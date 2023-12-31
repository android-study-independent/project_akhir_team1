package com.example.tanify.data.response.profile

import com.example.tanify.data.response.login.errordata
import com.google.gson.annotations.SerializedName

data class EditProfilResponse(
    @SerializedName("msg")
    val msg: String? = null,
    @SerializedName("error")
    val error: errordata? = null,
)
