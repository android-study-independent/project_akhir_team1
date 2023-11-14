package com.example.tanify.data.api.service

import com.example.tanify.data.data.LoginData
import com.example.tanify.data.data.RegisterData
import com.example.tanify.data.response.CurrentWeatherResponse
import com.example.tanify.data.response.LoginResponse
import com.example.tanify.data.response.RegisterRespons
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @POST("register")
    fun postRegister(
        @Body data: RegisterData
    ): Call<RegisterRespons>

    @POST("login")
    fun postLogin(
        @Body data: LoginData
    ): Call<LoginResponse>

    @GET("weather")
    fun getCurrentWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiid: String,
        @Query("units") unit: String="metric"
    ): Call<CurrentWeatherResponse>
}