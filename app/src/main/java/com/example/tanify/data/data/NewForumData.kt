package com.example.tanify.data.data

import okhttp3.MultipartBody
import java.io.File

data class NewForumData(
    val title: String,
    val content: String,
    val cover: MultipartBody.Part
)
