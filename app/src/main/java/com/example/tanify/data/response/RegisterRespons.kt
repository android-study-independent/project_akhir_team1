package com.example.tanify.data.response

import com.google.gson.annotations.SerializedName

data class RegisterRespons(
	@SerializedName("password")
	val password: String? = null,
	@SerializedName("konfirmasiPassword")
	val konfirmasiPassword: String? = null,
	@SerializedName("nama")
	val nama: String? = null,
	@SerializedName("email")
	val email: String? = null
)
