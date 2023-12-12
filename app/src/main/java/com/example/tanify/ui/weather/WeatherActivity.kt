package com.example.tanify.ui.weather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tanify.data.api.tanify.ApiConfig
import com.example.tanify.data.response.weather.WeeklyWeatherResponseItem
import com.example.tanify.databinding.ActivityWeatherBinding
import com.example.tanify.helper.formatDate
import com.example.tanify.ui.weather.items.ItemWeatherWeeklyAdapter
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WeatherActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWeatherBinding
    private lateinit var weatherAdapter: ItemWeatherWeeklyAdapter

    companion object {
        private const val TAG = "WeatherActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val latitude = intent.getDoubleExtra("latitude", 0.0)
        val longitude = intent.getDoubleExtra("longitude", 0.0)

        setView()
        setRecyclerView()
        setAction()
        getCurrentWeather(latitude, longitude)
    }

    private fun setView(){
        val location = intent.getStringExtra("location")
        val date = intent.getStringExtra("date")
        val temp = intent.getStringExtra("temp")
        val desc = intent.getStringExtra("desc")
        val iconPath = intent.getStringExtra("icon_path")
        val humidity = intent.getIntExtra("humidity", 0).toString()
        val windSpeed = intent.getIntExtra("wind_speed", 0).toString()

        binding.tvLocation.text = location
        binding.tvTanggal.text = formatDate(date!!)
        binding.tvDerajat.text = temp
        binding.tvCerahBerawan.text = desc
        binding.tvHumidity.text  = "${humidity}%"
        binding.tvWindSpeed.text = "$windSpeed KM/J"

        Picasso.get()
            .load(iconPath)
            .into(binding.ivCerahBerawan)
    }

    private fun setAction(){
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setRecyclerView(){
        weatherAdapter = ItemWeatherWeeklyAdapter(this, emptyList(),
            { weather ->
                Log.d("Kak Dhika", "ini weather")
            },
            {
                Log.d("Kak Dhika", "ini contoh")
            })
        binding.rvWeatherWeekly.layoutManager = LinearLayoutManager(this)
        binding.rvWeatherWeekly.adapter = weatherAdapter
    }

    private fun getCurrentWeather(long: Double?, lat: Double?) {
        showLoading(true)
        ApiConfig.instanceRetrofit.getWeeklyWeather(long!!, lat!!)
            .enqueue(object : Callback<List<WeeklyWeatherResponseItem>>{
                override fun onResponse(
                    call: Call<List<WeeklyWeatherResponseItem>>,
                    response: Response<List<WeeklyWeatherResponseItem>>
                ) {
                    showLoading(false)
                    if (response.isSuccessful) {
                        val weeklyWeatherResponse = response.body()

                        if (weeklyWeatherResponse != null) {
                            weatherAdapter.updateWeaklyData(weeklyWeatherResponse)
                        } else {
                            Toast.makeText(this@WeatherActivity, "Data tidak ditemukan", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@WeatherActivity, "Gagal mendapatkan data", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<WeeklyWeatherResponseItem>>, t: Throwable) {
                    Toast.makeText(this@WeatherActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "Error: ${t.message}")
                    showLoading(false)
                }
            })
    }

    //Loading circular
//    private fun showLoading(isLoading: Boolean){
//        if (isLoading) {
//            binding.progressCircular.visibility = View.VISIBLE
//        } else {
//            binding.progressCircular.visibility = View.GONE
//        }
//    }

    private fun showLoading(isLoading: Boolean){
        if (isLoading) {
            binding.linearLayout.visibility = View.GONE
            binding.shimmerView.visibility = View.VISIBLE
        } else {
            binding.linearLayout.visibility = View.VISIBLE
            binding.shimmerView.visibility = View.GONE
        }
    }
}