package com.example.tanify.ui.weather.items

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tanify.data.response.weather.WeeklyWeatherResponseItem
import com.example.tanify.databinding.ItemWeatherWeeklyBinding
import com.example.tanify.helper.formatDate
import com.example.tanify.helper.getDayFromDate

class ItemWeatherWeeklyAdapter(
    private val context: Context,
    private var listWeather: List<WeeklyWeatherResponseItem>
) : RecyclerView.Adapter<ItemWeatherWeeklyAdapter.ItemWeatherWeeklyHolder>() {
    inner class ItemWeatherWeeklyHolder(internal val binding: ItemWeatherWeeklyBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemWeatherWeeklyHolder {
        val binding = ItemWeatherWeeklyBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ItemWeatherWeeklyHolder(binding)
    }

    override fun getItemCount(): Int = listWeather.size

    override fun onBindViewHolder(holder: ItemWeatherWeeklyHolder, position: Int) {
        val dayWeather = listWeather[position]
        val posterPath = buildIconPath(dayWeather.path)
        val temp = "${dayWeather.temperature?.toInt().toString()}°C"

        holder.binding.tvTempWeekly.text = temp
        holder.binding.tvDescWeekly.text = dayWeather.description
        holder.binding.tvHariWeekly.text = getDayFromDate(dayWeather.date!!)
        holder.binding.tvTanggalWeekly.text = formatDate(dayWeather.date)


        Glide.with(context)
            .load(posterPath)
            .into(holder.binding.ivIconWeatherWeekly)
    }

    private fun buildIconPath(iconPath: String?): String {
        return "http://195.35.32.179:8001${iconPath}"
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateWeaklyData(newWeatherList: List<WeeklyWeatherResponseItem?>?){
        listWeather = newWeatherList as List<WeeklyWeatherResponseItem>
        notifyDataSetChanged()
    }
}