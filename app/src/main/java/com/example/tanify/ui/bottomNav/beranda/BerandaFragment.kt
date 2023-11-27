package com.example.tanify.ui.bottomNav.beranda

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.tanify.databinding.FragmentHomeBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.tanify.R
import com.example.tanify.data.api.tanify.ApiConfig
import com.example.tanify.data.data.ArtikelBerandaItemData
import com.example.tanify.data.data.FiturItemData
import com.example.tanify.data.response.CurrentWeatherResponse
import com.example.tanify.helper.weatherFormattedNumber
import com.example.tanify.ui.bottomNav.beranda.items.ItemBerandaArtikelAdapter
import com.example.tanify.ui.bottomNav.beranda.items.ItemFiturAdapter
import com.example.tanify.ui.login.LoginActivity
import com.example.tanify.ui.weather.WeatherActivity
import com.squareup.picasso.Picasso
import android.view.Window;
import android.view.WindowInsets
import android.view.WindowManager
import com.example.tanify.ui.artikel.ArtikelActivity

class BerandaFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val fiturList = ArrayList<FiturItemData>()
    private val artikelList = ArrayList<ArtikelBerandaItemData>()


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    companion object {
        private const val TAG = "BerandaFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val brandaViewModel =
            ViewModelProvider(this).get(BrandaViewModel::class.java)

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

//        binding.rvFiturUtama.setHasFixedSize(true)
//        binding.rvFiturUtama.layoutManager =
//            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        binding.rvArtikelBeranda.setHasFixedSize(true)
        binding.rvArtikelBeranda.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)


        addDataToList()
//        val adapterRvFitur = ItemFiturAdapter(fiturList)
//        binding.rvFiturUtama.adapter = adapterRvFitur

        val adapterRvArtikel = ItemBerandaArtikelAdapter(artikelList)
        binding.rvArtikelBeranda.adapter = adapterRvArtikel

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        getMyLocation()
        setAction()
//        getCurrentWeather(112.7747167,-7.2751638)
//        setStatusBar()
    }

    //On destroy
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("SetTextI18n")
    private fun setWeatherCardData(icon: String, temp: Double, city: String, description: String) {
        Log.d(TAG, weatherFormattedNumber(temp))
        binding.tvTemprature.text = "${weatherFormattedNumber(temp)}°"
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
        ApiConfig.instanceRetrofit.getCurrentWeather(long!!, lat!!)
            .enqueue(object : Callback<CurrentWeatherResponse> {
                override fun onResponse(
                    call: Call<CurrentWeatherResponse>,
                    response: Response<CurrentWeatherResponse>
                ) {
                    if (response.isSuccessful) {
                        val data = response.body()?.currentWeather
                        val temp = data?.temperature ?: 0.0
                        val city = data?.location ?: "none"
                        val icon = data?.path ?: "none"
                        val desc = data?.description ?: "none"
                        val humidity = data?.humidity
                        val windSpeed = data?.windSpeed
                        setWeatherCardData(icon, temp, city, desc)
                        setActionCard(
                            long,
                            lat,
                            city,
                            "26 Agustus 2022",
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
                }
            })
    }

    private fun setAction() {
        binding.btnFiturLms.setOnClickListener {
            Toast.makeText(requireContext(), "Fitur dalam pengembangan!", Toast.LENGTH_SHORT).show()
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

    private fun addDataToList() {

        fiturList.add(FiturItemData(R.drawable.lms_icon, "LMS"))

        artikelList.add(
            ArtikelBerandaItemData(
                R.drawable.gambar_contoh_petani, "Cara meningkatkan hasil pertanian\n" +
                        "dengan metode ini...", "31 Oktober 2023"
            )
        )
        artikelList.add(
            ArtikelBerandaItemData(
                R.drawable.gambar_contoh_petani, "Cara meningkatkan hasil pertanian\n" +
                        "dengan metode ini...", "31 Oktober 2023"
            )
        )
        artikelList.add(
            ArtikelBerandaItemData(
                R.drawable.gambar_contoh_petani, "Cara meningkatkan hasil pertanian\n" +
                        "dengan metode ini...", "31 Oktober 2023"
            )
        )
    }
}