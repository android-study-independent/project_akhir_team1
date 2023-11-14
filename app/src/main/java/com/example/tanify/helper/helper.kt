package com.example.tanify.helper

fun kelvinToCelcius(temprature: Double): Double {
    return temprature - 273.15
}
fun limitDecimalPlaces(value: Double, places: Int): String {
    return String.format("%.${places}f", value)

}

fun formattedNumber(number: Double): String{
    val formattedNumber = String.format("%.7f", number)
    val split = formattedNumber.split(".");
    val str = StringBuilder(split[1])
    str.insert(3, ',')
    return "${split[0]}.${str}"
}