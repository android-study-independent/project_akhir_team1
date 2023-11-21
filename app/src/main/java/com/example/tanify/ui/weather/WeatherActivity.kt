package com.example.tanify.ui.weather

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tanify.R
import com.example.tanify.data.api.tanify.ApiConfig
import com.example.tanify.data.response.CurrentWeatherResponse
import com.example.tanify.data.response.WeeklyWeatherResponse
import com.example.tanify.data.response.WeeklyWeatherResponseItem
import com.example.tanify.databinding.ActivityWeatherBinding
import com.example.tanify.ui.bottomNav.beranda.BerandaFragment
import com.example.tanify.ui.weather.items.ItemWeatherWeeklyAdapter
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WeatherActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWeatherBinding
    private lateinit var adapter: ItemWeatherWeeklyAdapter
    private var listWeeklyWeather = mutableListOf<WeeklyWeatherResponseItem>()

    companion object {
        private const val TAG = "WeatherActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val latitude = intent.getDoubleExtra("latitude", 0.0)
        val longitude = intent.getDoubleExtra("longitude", 0.0)

        setRv()
        setAction()
        getCurrentWeather(longitude, latitude)
    }

    private fun setAction(){
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setRv(){
        binding.rvWeatherWeekly.layoutManager = LinearLayoutManager(this)
        adapter = ItemWeatherWeeklyAdapter(listWeeklyWeather)
        binding.rvWeatherWeekly.adapter = adapter
    }

    private fun getCurrentWeather(long: Double?, lat: Double?) {
        ApiConfig.instanceRetrofit.getWeeklyWeather(long!!, lat!!)
            .enqueue(object : Callback<WeeklyWeatherResponse> {
                override fun onResponse(
                    call: Call<WeeklyWeatherResponse>,
                    response: Response<WeeklyWeatherResponse>
                ) {
                    if (response.isSuccessful) {
                        val weeklyWeatherResponse = response.body()
                        if (weeklyWeatherResponse != null && weeklyWeatherResponse.weeklyWeatherResponse != null){
                            listWeeklyWeather.clear()
                            //listWeeklyWeather.addAll(weeklyWeatherResponse.weeklyWeatherResponse!!)
                            adapter.notifyDataSetChanged()
                        }
                    } else {
                        Log.e(TAG, "onFailure: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<WeeklyWeatherResponse>, t: Throwable) {
                    Log.e(TAG, "onFailure (OF): ${t.message.toString()}")
                }

            })
    }
}