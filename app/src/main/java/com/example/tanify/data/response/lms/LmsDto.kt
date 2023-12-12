package com.example.tanify.data.response.lms

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class LmsDto(
    var cover: String? = null,
    val createdAt: String? = null,
    val view: Int? = null,
    val section: Int? = null,
    val id: Int? = null,
    val title: String? = null,
    val totalsection: Int? = null,
    val progres: Int? = null
): Parcelable
