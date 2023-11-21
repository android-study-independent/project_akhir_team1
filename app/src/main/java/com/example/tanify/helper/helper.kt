package com.example.tanify.helper

import android.util.Log
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


fun formatDate(inputDateString: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    val date = inputFormat.parse(inputDateString)

    val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
    return outputFormat.format(date!!)
}

fun getDayFromDate(dateString: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    val date = inputFormat.parse(dateString)

    val outputFormat = SimpleDateFormat("EEEE", Locale.getDefault())
    return outputFormat.format(date!!)
}

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