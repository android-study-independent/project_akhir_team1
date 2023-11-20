package com.example.tanify.helper

import android.content.Context
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.tanify.R
import com.example.tanify.data.api.weather.ApiWeatherConfig
import com.example.tanify.data.response.CurrentWeatherResponse
import com.example.tanify.ui.bottomNav.beranda.BerandaFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private fun getCurrentWeather(lat: Double, lon: Double){
    val apiKey = "8b0d3e065374bc20917c353d319277e0"
    ApiWeatherConfig.intanceRetrofit.getCurrentWeather(lat, lon, apiKey)
        .enqueue(object : Callback<CurrentWeatherResponse> {
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
                    val setTemp = limitDecimalPlaces(celciusTemp, 2)
                    setWeatherCardData(setTemp, kota.toString(), description.toString() ,iconCode.toString())
                }
            }

            override fun onFailure(call: Call<CurrentWeatherResponse>, t: Throwable) {
//                Log.e(BerandaFragment.TAG, "onFailur: ${t.message}")
            }
        })
}

private fun setWeatherCardData(temprature: String, kota: String, description: String, iconCode: String){
    val celciusTemprature = kelvinToCelcius(temprature.toDouble())
//    binding.tvTemprature.text = celciusTemprature.toString()
//    binding.tvDaerah.text = kota
//    binding.tvDeskripsi.text = description
//    setWeatherIcon(requireContext(), iconCode, binding.icWeather)
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