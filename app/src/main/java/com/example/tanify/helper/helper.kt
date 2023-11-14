package com.example.tanify.helper

fun kelvinToCelcius(temprature: Double): Double {
    return temprature - 273.15
}
fun limitDecimalPlaces(value: Double, places: Int): String {
    return String.format("%.${places}°C", value)
}