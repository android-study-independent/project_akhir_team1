package com.example.tanify.helper

fun kelvinToCelcius(temprature: Double?): Double {
    val kelvinScale: Double = 273.15
    val celcius = temprature?.minus(kelvinScale)
    return celcius?.toDouble() ?: 0.0
}
fun limitDecimalPlaces(value: Double, places: Int): String {
    return String.format("%.${places}f", value)
}