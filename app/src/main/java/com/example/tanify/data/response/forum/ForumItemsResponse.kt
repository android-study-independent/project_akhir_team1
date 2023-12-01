package com.example.tanify.data.response.forum

import com.google.gson.annotations.SerializedName

data class ForumItemsResponse(

	@field:SerializedName("msg")
	val msg: String? = null,

	@field:SerializedName("data")
	val data: List<DataItem>? = null
)

data class DataItem(

	@field:SerializedName("cover")
	val cover: String? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("id_user")
	val idUser: Int? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("content")
	val content: String? = null,

	@field:SerializedName("likes")
	val likes: List<LikesItem>? = null
)

data class LikesItem(

	@field:SerializedName("id_user")
	val idUser: Int? = null
)
