package com.example.tanify.data.response

import com.google.gson.annotations.SerializedName

data class WeeklyWeatherResponse(

	@field:SerializedName("WeeklyWeatherResponse")
	val weeklyWeatherResponse: List<WeeklyWeatherResponseItem?>? = null
)

data class WeeklyWeatherResponseItem(

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("path")
	val path: String? = null,

	@field:SerializedName("temperature")
	val temperature: Any? = null,

	@field:SerializedName("icon")
	val icon: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("main")
	val main: String? = null
)
