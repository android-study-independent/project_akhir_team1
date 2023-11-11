package com.example.tanify.data.api

import com.example.tanify.data.data.LoginData
import com.example.tanify.data.data.RegisterData
import com.example.tanify.data.response.LoginResponse
import com.example.tanify.data.response.RegisterRespons
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {

    @POST("registrasi")
    fun postRegister(
        @Body data: LoginData
    ): Call<RegisterRespons>

    @POST("login")
    fun postLogin(
        @Body data: LoginData
    ): Call<LoginResponse>
}