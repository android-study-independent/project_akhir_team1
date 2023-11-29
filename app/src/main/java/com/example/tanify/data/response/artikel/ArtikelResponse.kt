package com.example.tanify.data.response.artikel

import com.google.gson.annotations.SerializedName

data class ArtikelResponse(

	@field:SerializedName("msg")
	val msg: String? = null,

	@field:SerializedName("data")
	val data: List<Artikel?>? = null
)

data class Artikel(

	@field:SerializedName("cover")
	val cover: String? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("deskripsi")
	val deskripsi: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("isi")
	val isi: String? = null,

	@field:SerializedName("pembuat")
	val pembuat: String? = null
)
