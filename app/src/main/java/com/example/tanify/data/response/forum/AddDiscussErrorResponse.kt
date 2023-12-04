package com.example.tanify.data.response.forum

import com.google.gson.annotations.SerializedName

data class AddDiscussErrorResponse(

	@field:SerializedName("errors")
	val errors: List<ErrorItem?>? = null
)

data class ErrorItem(

	@field:SerializedName("msg")
	val msg: String? = null,

	@field:SerializedName("path")
	val path: String? = null,

	@field:SerializedName("location")
	val location: String? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("value")
	val value: String? = null
)
