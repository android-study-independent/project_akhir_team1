package com.example.tanify.helper

fun kelvinToCelcius(temprature: Double?): Double {
    val kelvinScale: Double = 273.15
    val celcius = temprature?.minus(kelvinScale)
    return celcius?.toDouble() ?: 0.0
}