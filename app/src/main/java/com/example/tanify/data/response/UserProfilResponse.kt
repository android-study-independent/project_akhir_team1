package com.example.tanify.data.response

import com.google.gson.annotations.SerializedName

data class UserProfilResponse(
    @SerializedName("nama")
    val nama: String? = null,
    @SerializedName("email")
    val email: String? = null,
    @SerializedName("photo")
    val photo: String? = null,
)
