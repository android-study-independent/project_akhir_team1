package com.example.tanify.data.response.lms

import com.google.gson.annotations.SerializedName

data class lessonAllResponse(
    @field:SerializedName("msg")
    val msg: String,

    @field:SerializedName("rekomendasi")
    val rekomendasi: List<ModulItem>,

    @field:SerializedName("modul")
    val modul: List<ModulItem>,

    @field:SerializedName("user")
    val user: UserLms

)

data class searchResponse(
    @field:SerializedName("msg")
    val msg: String,

    @field:SerializedName("data")
    val data: List<ModulItem>,

)



data class ModulItem(

    @field:SerializedName("cover")
    val cover: String,

    @field:SerializedName("createdAt")
    val createdAt: String,

    @field:SerializedName("view")
    val view: Int,

    @field:SerializedName("section")
    val section: Int,

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("title")
    val title: String
)

data class UserLms(

    @field:SerializedName("nama")
    val nama: String,

    @field:SerializedName("photo")
    val photo: String
)

