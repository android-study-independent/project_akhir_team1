package com.example.tanify.data.api

import com.example.tanify.data.response.LoginResponse
import com.example.tanify.data.response.RegisterRespons
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {
    @FormUrlEncoded
    @POST("registrasi")
    fun postRegister(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("konfirmasiPassword") konfirmasiPassword: String
    ): Call<RegisterRespons>

    @FormUrlEncoded
    @POST("login")
    fun postLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>
}