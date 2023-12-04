package com.example.tanify.data.response.forum

import com.google.gson.annotations.SerializedName

data class AddDiscussResponse(

	@field:SerializedName("msg")
	val msg: String? = null,

	@field:SerializedName("data")
	val data: DataForum? = null
)

data class CreatedAt(

	@field:SerializedName("val")
	val vall: String? = null
)

data class DataForum(

	@field:SerializedName("cover")
	val cover: String? = null,

	@field:SerializedName("createdAt")
	val createdAt: CreatedAt? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("id_user")
	val idUser: Int? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("content")
	val content: String? = null
)
