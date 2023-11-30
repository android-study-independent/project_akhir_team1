package com.example.tanify.data.response.login

import com.google.gson.annotations.SerializedName

data class RegisterRespons(
    @SerializedName("msg")
	val message: String? = null,
    @SerializedName("data")
	val data: data? = null,
    @SerializedName("error")
	val error: error? = null,

    )

data class data(
	@SerializedName("id")
	val id: String? = null,
	@SerializedName("nama")
	val nama: String? = null,
	@SerializedName("email")
	val email: String? = null,

)

data class error(
    @SerializedName("nama")
	val nama: errordata? = null,
    @SerializedName("email")
	val email: errordata? = null,
    @SerializedName("password")
	val password: errordata? = null,
    @SerializedName("konfirmasiPassword")
	val konfirmasiPassword: errordata? = null,
	@SerializedName("foto")
	val foto: errordata? = null,

    )

data class errordata(
	@SerializedName("type")
	val type: String? = null,
	@SerializedName("value")
	val value: String? = null,
	@SerializedName("msg")
	val msg: String? = null,
	@SerializedName("path")
	val path: String? = null,
	@SerializedName("location")
	val location: String? = null,

)




