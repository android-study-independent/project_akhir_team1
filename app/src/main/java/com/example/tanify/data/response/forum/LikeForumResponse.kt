package com.example.tanify.data.response.forum

import com.google.gson.annotations.SerializedName

data class LikeForumResponse(

	@field:SerializedName("msg")
	val msg: String? = null,

	@field:SerializedName("data")
	val data: DataLike? = null
)

data class DataLike(

	@field:SerializedName("isLike")
	val isLike: Boolean? = null,

	@field:SerializedName("id_post")
	val idPost: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("id_user")
	val idUser: Int? = null
)
