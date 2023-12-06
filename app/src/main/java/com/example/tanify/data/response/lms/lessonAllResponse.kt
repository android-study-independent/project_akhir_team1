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

data class lessonByIdResponse(
    @field:SerializedName("msg")
    val msg: String,

    @field:SerializedName("lesson")
    val lesson: ModulItem,

    @field:SerializedName("section")
    val section: List<SectionItem>,

)

data class SectionyIdResponse(
    @field:SerializedName("msg")
    val msg: String,

    @field:SerializedName("data")
    val data: SectionItem,

    )

data class ProgresResponse(
    @field:SerializedName("msg")
    val msg: String,

    @field:SerializedName("progres")
    val progres: List<ModulItem>,

    )




