package com.example.tanify.data.response.lms

import com.google.gson.annotations.SerializedName

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

data class SectionItem(

    @field:SerializedName("id_section")
    val id_section: String,

    @field:SerializedName("section")
    val section: String,

    @field:SerializedName("progres")
    val progres: Boolean

)

