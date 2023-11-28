package com.example.tanify.data.response

import com.google.gson.annotations.SerializedName

data class EditProfilResponse(
    @SerializedName("msg")
    val msg: String? = null,
    @SerializedName("error")
    val error: errordata? = null,
)
