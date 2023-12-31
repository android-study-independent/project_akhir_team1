package com.example.tanify.helper

import android.util.Log
import android.widget.TextView
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit


fun formatDate(inputDateString: String): String {
    try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = inputFormat.parse(inputDateString)

        if (date != null) {
            val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
            return outputFormat.format(date)
        } else {
            return "Invalid Date"
        }
    } catch (e: ParseException) {
        e.printStackTrace()
        return "Invalid Date"
    }
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

fun weatherFormattedNumber(number: Double): String {
    val formattedNumber = number.toInt().toString()
    return formattedNumber
}

fun getFirstName(fullName: String): String {
    val nameParts = fullName.split(" ")
    val firstName = nameParts.firstOrNull()

    return firstName ?: ""
}

fun setWordLimit(textView: TextView, originalText: String, wordLimit: Int) {
    val words = originalText.split(" ")
    val truncatedText = words.take(wordLimit).joinToString(" ")

    textView.text = "${truncatedText}..."
}

fun getTimeAgo(createdAt: String): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    sdf.timeZone = TimeZone.getTimeZone("UTC")

    try {
        val date: Date = sdf.parse(createdAt)
        val timeInMillis: Long = date.time

        val now = System.currentTimeMillis()
        val diff = now - timeInMillis

        val seconds = TimeUnit.MILLISECONDS.toSeconds(diff)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(diff)
        val hours = TimeUnit.MILLISECONDS.toHours(diff)

        return when {
            seconds < 60 -> "$seconds detik yang lalu"
            minutes < 60 -> "$minutes menit yang lalu"
            else -> "$hours jam yang lalu"
        }
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return ""
}


