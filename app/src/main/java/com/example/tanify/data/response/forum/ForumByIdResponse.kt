package com.example.tanify.data.response.forum

import com.google.gson.annotations.SerializedName

data class ForumByIdResponse(

	@field:SerializedName("msg")
	val msg: String? = null,

	@field:SerializedName("data")
	val data: Data? = null
)

data class CommentItem(

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("id_post")
	val idPost: Int? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("id_user")
	val idUser: Int? = null,

	@field:SerializedName("text")
	val text: String? = null,

	@field:SerializedName("user")
	val user: User? = null
)

data class Data(

	@field:SerializedName("cover")
	val cover: String? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("created_by")
	val createdBy: User? = null,

	@field:SerializedName("comment")
	val comment: List<CommentItem?>? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("id_user")
	val idUser: Int? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("content")
	val content: String? = null
)

data class User(

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("photo")
	val photo: String? = null
)
