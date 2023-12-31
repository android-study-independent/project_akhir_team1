package com.example.tanify.ui.bottomNav.beranda

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.tanify.R
import com.example.tanify.databinding.FragmentHomeBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.tanify.data.api.tanify.ApiConfig
import com.example.tanify.data.response.artikel.Artikel
import com.example.tanify.data.response.artikel.ArtikelResponse
import com.example.tanify.data.response.profile.UserProfilResponse
import com.example.tanify.data.response.weather.CurrentWeatherResponse
import com.example.tanify.helper.GetUserProfilCallback
import com.example.tanify.helper.getUserProfil
import com.example.tanify.helper.weatherFormattedNumber
import com.example.tanify.ui.bottomNav.beranda.items.ItemBerandaArtikelAdapter
import com.example.tanify.ui.weather.WeatherActivity
import com.squareup.picasso.Picasso
import com.example.tanify.ui.artikel.ArtikelActivity
import com.example.tanify.ui.artikel.DetailArtikelActivity
import com.example.tanify.ui.bottomNav.profile.ProfileFragment
import com.example.tanify.ui.lms.LmsActivity
import com.google.gson.Gson

class BerandaFragment : Fragment(), ItemBerandaArtikelAdapter.OnArtikelBerandaItemClickListener {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var artikelBerandaAdapter: ItemBerandaArtikelAdapter
    private lateinit var sharedPreferences: SharedPreferences
    //private val fiturList = ArrayList<FiturItemData>()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var dataprofil: UserProfilResponse? = null

    companion object {
        private const val TAG = "BerandaFragment"
        private var TOKEN = "token"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val brandaViewModel =
            ViewModelProvider(this).get(BrandaViewModel::class.java)

        Log.d("Beranda:", "Panggil beranda onCreateView")

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val textView: TextView = binding.textHome
//        brandaViewModel.text.observe(viewLifecycleOwner) {
//             textView.text = it
//        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = requireContext().getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        TOKEN = sharedPreferences.getString("token", "").toString()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        getProfil()
        getMyLocation()
        setAction()
        setRecyclerView()
        getArtikelData()
    }

    private fun getProfil() {
        getUserProfil(TOKEN, object :GetUserProfilCallback{
            override fun onUserProfileReceived(userProfil: UserProfilResponse) {
                dataprofil = userProfil
                // set profil
                saveProfilToSharedPreferences(dataprofil)
                val foto = dataprofil?.photo
                Glide.with(requireContext())
                    .load(foto)
                    .skipMemoryCache(false)
                    .placeholder(R.drawable.ic_profile_blank)
                    .error(R.drawable.ic_profile_blank)
                    .into(binding.ivProfile)
                binding.textView3.text = dataprofil?.nama

            }

            override fun onFailed(message: String) {
                Log.e(TAG, "get Profile: ${message}")
            }

        })
    }
    private fun saveProfilToSharedPreferences(profilData: UserProfilResponse?) {
        val gson = Gson()
        val profilDataString = gson.toJson(profilData)

        with(sharedPreferences.edit()) {
            putString("profil", profilDataString)
            apply()
        }
    }

    //On destroy
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setRecyclerView(){
        artikelBerandaAdapter = ItemBerandaArtikelAdapter(requireContext(), emptyList())
        artikelBerandaAdapter.setOnArtikelBerandaItemClickListener(this)
        binding.rvArtikelBeranda.layoutManager = LinearLayoutManager(requireContext())
        binding.rvArtikelBeranda.adapter = artikelBerandaAdapter
    }

    @SuppressLint("SetTextI18n")
    private fun setWeatherCardData(icon: String, temp: Double, city: String, description: String) {
        Log.d(TAG, weatherFormattedNumber(temp))
        binding.tvTemprature.text = "${weatherFormattedNumber(temp)}°C"
        binding.tvDaerah.text = city
        binding.tvDeskripsi.text = description
        val iconPath = buildIconPath(icon)
        //val path = buildIconPath(icon)
        Log.d(TAG, iconPath)
        Picasso.get().load(iconPath).into(binding.icWeather)
    }

    private fun buildIconPath(iconPath: String): String {
        return "http://195.35.32.179:8001${iconPath}"
    }

    private fun getCurrentWeather(long: Double?, lat: Double?) {
        showLoadingCardWeather(true)
        ApiConfig.instanceRetrofit.getCurrentWeather(long!!, lat!!)
            .enqueue(object : Callback<CurrentWeatherResponse> {
                override fun onResponse(
                    call: Call<CurrentWeatherResponse>,
                    response: Response<CurrentWeatherResponse>
                ) {
                    showLoadingCardWeather(false)
                    if (response.isSuccessful) {
                        val data = response.body()?.currentWeather
                        val temp = data?.temperature ?: 0.0
                        val city = data?.location ?: "none"
                        val icon = data?.path ?: "none"
                        val desc = data?.description ?: "none"
                        val humidity = data?.humidity
                        val windSpeed = data?.windSpeed
                        val date = data?.date!!
                        setWeatherCardData(icon, temp, city, desc)
                        setActionCard(
                            long,
                            lat,
                            city,
                            date,
                            "${weatherFormattedNumber(temp)}°C",
                            desc,
                            buildIconPath(icon),
                            humidity!!.toInt(),
                            windSpeed!!.toInt()
                        )
                    } else {
                        Log.e(TAG, "onFailur: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<CurrentWeatherResponse>, t: Throwable) {
                    Log.e(TAG, "onFailure (OF): ${t.message.toString()}")
                    showLoadingCardWeather(false)
                }
            })
    }

    private fun setAction() {
        binding.btnFiturLms.setOnClickListener {
//            Toast.makeText(requireContext(), "Fitur dalam pengembangan!", Toast.LENGTH_SHORT).show()
             val intent = Intent(requireContext(), LmsActivity::class.java)
             requireContext().startActivity(intent)
        }
        binding.btnHalamanArtikel.setOnClickListener {
            val intent = Intent(requireContext(), ArtikelActivity::class.java)
            requireContext().startActivity(intent)
        }
    }

    private fun setActionCard(
        lat: Double,
        long: Double,
        location: String,
        date: String,
        temp: String,
        desc: String,
        iconPath: String,
        humidity: Int,
        windSpeed: Int
    ) {
        binding.cardViewWeather.setOnClickListener {
            //membawa perubahan langsung dari card beranda
            val intent = Intent(requireContext(), WeatherActivity::class.java)
            intent.putExtra("latitude", lat)
            intent.putExtra("longitude", long)
            intent.putExtra("location", location)
            intent.putExtra("date", date)
            intent.putExtra("temp", temp)
            intent.putExtra("desc", desc)
            intent.putExtra("icon_path", iconPath)
            intent.putExtra("humidity", humidity)
            intent.putExtra("wind_speed", windSpeed)
            startActivity(intent)
        }
    }

    private fun getArtikelData() {
        showLoading(true)
        ApiConfig.instanceRetrofit.getArtikel()
            .enqueue(object : Callback<ArtikelResponse>{
                override fun onResponse(
                    call: Call<ArtikelResponse>,
                    response: Response<ArtikelResponse>
                ) {
                    showLoading(false)
                    if (response.isSuccessful) {
                        val artikelResponse = response.body()

                        // Update RecyclerView with the received data
                        if (artikelResponse?.data != null) {
                            artikelBerandaAdapter.updateDataArtikelBeranda(artikelResponse.data)
                        } else {
                            // Handle jika data dari API kosong atau tidak valid
                            Toast.makeText(requireContext(), "Data tidak ditemukan", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        // Handle jika respon tidak berhasil (misalnya kode respon bukan 200 OK)
                        Toast.makeText(requireContext(), "Gagal mendapatkan data", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ArtikelResponse>, t: Throwable) {
                    Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    showLoading(false)
                }
            })
    }

    //Get user lat & lon
    private fun getMyLocation() {
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) && checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    val lat = it.latitude
                    val lon = it.longitude
                    Log.d(TAG, "lat = $lat\nlon = $lon")
                    getCurrentWeather(lon, lat)
                } ?: run {
                    Log.e(TAG, "Last location is null")
                }
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    //User permission request
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    // Precise location access granted.
                    getMyLocation()
                }

                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    // Only approximate location access granted.
                    getMyLocation()
                }

                else -> {
                    // No location access granted.
                }
            }
        }

    //User permission checker
    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun showLoading(isLoading: Boolean){
        if (isLoading) {
            binding.rvArtikelBeranda.visibility = View.GONE
            binding.shimmerView.visibility = View.VISIBLE
        } else {
            binding.rvArtikelBeranda.visibility = View.VISIBLE
            binding.shimmerView.visibility = View.GONE
        }
    }

    private fun showLoadingCardWeather(isLoading: Boolean){
        if (isLoading) {
            binding.progressCircular.visibility = View.VISIBLE
        } else {
            binding.progressCircular.visibility = View.GONE
        }
    }

    override fun onArtikelBerandaItemClickListener(artikel: Artikel) {
        val id = artikel.id
        Log.d(TAG, "id artikel yang dipilih = $id")
        val intent = Intent(requireContext(), DetailArtikelActivity::class.java)
        intent.putExtra("ARTIKEL_ID", artikel.id)
        startActivity(intent)
    }
}