package com.example.tanify.ui.bottomNav.beranda

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.tanify.data.api.weather.ApiWeatherConfig
import com.example.tanify.data.response.CurrentWeatherResponse
import com.example.tanify.databinding.FragmentHomeBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.tanify.R
import com.example.tanify.helper.formattedNumber
import com.example.tanify.helper.kelvinToCelcius
import com.example.tanify.helper.limitDecimalPlaces

class BerandaFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient

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

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        getMyLocation()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setWeatherCardData(temprature: String, kota: String, description: String, iconCode: String){
        val celciusTemprature = kelvinToCelcius(temprature.toDouble())
        binding.tvTemprature.text = celciusTemprature.toString()
        binding.tvDaerah.text = kota
        binding.tvDeskripsi.text = description
        setWeatherIcon(requireContext(), iconCode, binding.icWeather)
    }

    private fun setWeatherIcon(context: Context, iconCode: String?, imageView: ImageView){
        if (!iconCode.isNullOrEmpty()) {
            val iconUrl = "https://openweathermap.org/img/w/${iconCode}.png"

            Glide.with(context)
                .load(iconUrl)
                .into(imageView)
        } else {
            Glide.with(context)
                .load(R.drawable.awan_matahari)
                .into(imageView)
        }
    }

    private fun getCurrentWeather(lat: Double, lon: Double){
        val apiKey = "8b0d3e065374bc20917c353d319277e0"
        ApiWeatherConfig.intanceRetrofit.getCurrentWeather(lat, lon, apiKey)
            .enqueue(object : Callback<CurrentWeatherResponse>{
                override fun onResponse(
                    call: Call<CurrentWeatherResponse>,
                    response: Response<CurrentWeatherResponse>
                ) {
                    if (response.isSuccessful) {
                        val currentWeather = response.body()
                        val temprature = currentWeather?.main?.temp
                        val iconCode = currentWeather?.weather?.get(0)?.icon
                        val kota = currentWeather?.name
                        val description = currentWeather?.weather?.get(0)?.description

                        val celciusTemp = kelvinToCelcius(temprature!!)
                       // val setTemp = formattedNumber(temprature!!.toDouble())
                        Log.d(TAG,"suhu: $kota")
                       // setWeatherCardData(setTemp, kota.toString(), description.toString() ,iconCode.toString())
                    }
                }

                override fun onFailure(call: Call<CurrentWeatherResponse>, t: Throwable) {
                    Log.e(TAG, "onFailur: ${t.message}")
                }
            })
    }

    private fun getMyLocation() {
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) && checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    val lat = it.latitude
                    val lon = it.longitude
                    Log.d(TAG, "lat = $lat\nlon = $lon")
                    getCurrentWeather(lat, lon)
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
    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }
}