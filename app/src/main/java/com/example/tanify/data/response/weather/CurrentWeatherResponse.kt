package com.example.tanify.data.response.weather

import com.google.gson.annotations.SerializedName

data class CurrentWeatherResponse(

	@field:SerializedName("currentWeather")
	val currentWeather: CurrentWeather? = null
)

data class CurrentWeather(

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("path")
	val path: String? = null,

	@field:SerializedName("wind speed")
	val windSpeed: Double? = null,

	@field:SerializedName("temperature")
	val temperature: Double? = null,

	@field:SerializedName("icon")
	val icon: String? = null,

	@field:SerializedName("humidity")
	val humidity: Int? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("main")
	val main: String? = null,

	@field:SerializedName("location")
	val location: String? = null
)
