package com.example.tanify.data.response.lms

import com.google.gson.annotations.SerializedName

data class ModulItem(

    @field:SerializedName("cover")
    val cover: String? = null,

    @field:SerializedName("createdAt")
    val createdAt: String? = null,

    @field:SerializedName("view")
    val view: Int? = null,

    @field:SerializedName("section")
    val section: Int? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("title")
    val title: String? = null,

    // untuk get progress
    @field:SerializedName("total section")
    val totalsection: Int? = null,

    @field:SerializedName("progres")
    val progres: Int? = null,

)

data class UserLms(

    @field:SerializedName("nama")
    val nama: String? = null,

    @field:SerializedName("photo")
    val photo: String? = null,
)

data class SectionItem(

    @field:SerializedName("id_section")
    val id_section: String? = null,

    @field:SerializedName("section")
    val section: String? = null,

    @field:SerializedName("progres")
    val progres: Boolean? = null,

    // untuk data section by id
    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("id_lms")
    val id_lms: String? = null,

    @field:SerializedName("content")
    val content: String? = null,

    @field:SerializedName("cover")
    val cover: String ? = null,
    )
data class dataPutProgres(
    @field:SerializedName("id")
    val id: String? = null,
    @field:SerializedName("id_user")
    val id_user: String? = null,
    @field:SerializedName("id_lms")
    val id_lms: String? = null,
    @field:SerializedName("id_section")
    val id_section: String? = null,
    @field:SerializedName("progres")
    val progres: String? = null,
)

