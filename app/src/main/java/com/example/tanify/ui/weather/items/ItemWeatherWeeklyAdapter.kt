package com.example.tanify.ui.weather.items

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextClock
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tanify.data.response.WeeklyWeatherResponse
import com.example.tanify.R
import com.example.tanify.data.response.WeeklyWeatherResponseItem
import com.example.tanify.helper.formatDate
import com.example.tanify.helper.getDayFromDate
import com.squareup.picasso.Picasso

class ItemWeatherWeeklyAdapter(private val listWeather: List<WeeklyWeatherResponseItem>) :
    RecyclerView.Adapter<ItemWeatherWeeklyAdapter.ItemWeatherWeeklyHolder>() {
    inner class ItemWeatherWeeklyHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        @SuppressLint("SetTextI18n")
        fun bindView(weather: WeeklyWeatherResponseItem) {
            val tvTemp = view.findViewById<TextView>(R.id.tv_temp_weekly)
            val tvDesc = view.findViewById<TextView>(R.id.tv_desc_weekly)
            val tvDay = view.findViewById<TextView>(R.id.tv_hari_weekly)
            val tvDate = view.findViewById<TextView>(R.id.tv_tanggal_weekly)
            val ivIcon = view.findViewById<ImageView>(R.id.iv_icon_weather_weekly)
            val formattedTemp = weather.temperature?.let { String.format("%.1f", it) } ?: ""
            val path = buildIconPath(weather.path)

            tvTemp.text = "$formattedTemp°C"
            tvDesc.text = weather.description
            tvDay.text = getDayFromDate(weather.date.toString())
            tvDate.text = formatDate(weather.date.toString())
            Picasso.get().load(path).into(ivIcon)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemWeatherWeeklyHolder =
        ItemWeatherWeeklyHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_weather_weekly, parent, false)
        )

    override fun getItemCount(): Int = listWeather.size

    override fun onBindViewHolder(holder: ItemWeatherWeeklyHolder, position: Int) = holder.bindView(listWeather[position])

    private fun buildIconPath(iconPath: String?): String {
        return "http://195.35.32.179:8001${iconPath}"
    }
}