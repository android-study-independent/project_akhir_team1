package com.example.tanify.data.response.login

import com.google.gson.annotations.SerializedName

data class LoginResponse(
	@SerializedName("msg")
	val message: String? = null,
	@SerializedName("token")
	val token: String? = null
)
