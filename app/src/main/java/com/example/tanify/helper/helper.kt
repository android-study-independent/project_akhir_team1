package com.example.tanify.helper

import android.util.Log
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun calculateElapsedTime(createdDateTime: String): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val currentDate = Calendar.getInstance().time
    val createdAtDate = sdf.parse(createdDateTime)

    val diff = StrictMath.abs(currentDate.time - createdAtDate!!.time)
    val diffMinutes = (diff / (60 * 1000)) % 60

    return "$diffMinutes"
}

fun formattedNumber(number: Double): String{
    val formattedNumber = String.format("%.7f", number)
    val split = formattedNumber.split(".");
    val str = StringBuilder(split[1])
    str.insert(3, ',')
    return "${split[0]}.${str}"

}