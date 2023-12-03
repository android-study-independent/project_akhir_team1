package com.example.tanify.data.response.forum

import com.google.gson.annotations.SerializedName

data class CommentResponse(

	@field:SerializedName("errors")
	val errors: List<ErrorsItem?>? = null
)

data class ErrorsItem(

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
