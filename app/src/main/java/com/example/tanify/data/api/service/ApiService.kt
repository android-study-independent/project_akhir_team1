package com.example.tanify.data.api.service

import com.example.tanify.data.data.LoginData
import com.example.tanify.data.data.RegisterData
import com.example.tanify.data.response.Artikel
import com.example.tanify.data.response.ArtikelResponse
import com.example.tanify.data.response.CurrentWeatherResponse
import com.example.tanify.data.response.WeeklyWeatherResponse
import com.example.tanify.data.response.LoginResponse
import com.example.tanify.data.response.RegisterRespons
import retrofit2.Call
import retrofit2.http.Body
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

    @GET("weather/weekly")
    fun getWeeklyWeather(
        @Query("longitude") longitude: Double,
        @Query("latitude") latitude: Double
    ): Call<WeeklyWeatherResponse>

    @GET("weather/current")
    fun getCurrentWeather(
        @Query("longitude") longitude: Double,
        @Query("latitude") latitude: Double
    ): Call<CurrentWeatherResponse>

    @GET("artikel")
    fun getArtikel(): Call<ArtikelResponse>
}