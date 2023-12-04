package com.example.tanify.data.api.service

import com.example.tanify.data.data.CommentData
import com.example.tanify.data.data.EditPassword
import com.example.tanify.data.data.LoginData
import com.example.tanify.data.data.RegisterData
import com.example.tanify.data.data.searchdatalms
import com.example.tanify.data.response.profile.EditPasswordResponse
import com.example.tanify.data.response.EditProfilResponse
import okhttp3.MultipartBody
import com.example.tanify.data.response.artikel.ArtikelResponse
import com.example.tanify.data.response.forum.CommentResponse
import com.example.tanify.data.response.forum.ForumByIdResponse
import com.example.tanify.data.response.forum.ForumItemsResponse
import com.example.tanify.data.response.lms.lessonAllResponse
import com.example.tanify.data.response.lms.searchResponse
import com.example.tanify.data.response.weather.CurrentWeatherResponse
import com.example.tanify.data.response.login.LoginResponse
import com.example.tanify.data.response.login.RegisterRespons
import com.example.tanify.data.response.profile.UserProfilResponse
import com.example.tanify.data.response.weather.WeeklyWeatherResponseItem
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @POST("register")
    fun postRegister(
        @Body data: RegisterData,
    ): Call<RegisterRespons>

    @POST("login")
    fun postLogin(
        @Body data: LoginData,
    ): Call<LoginResponse>

    @GET("weather/weekly")
    fun getWeeklyWeather(
        @Query("longitude") longitude: Double,
        @Query("latitude") latitude: Double,
    ): Call<List<WeeklyWeatherResponseItem>>

    @GET("weather/current")
    fun getCurrentWeather(
        @Query("longitude") longitude: Double,
        @Query("latitude") latitude: Double,
    ): Call<CurrentWeatherResponse>

    @GET("artikel")
    fun getArtikel(): Call<ArtikelResponse>

    @GET("profile/user-profile")
    fun getUserProfil(
        @Header("Authorization") authorization: String,
    ): Call<UserProfilResponse>

    @Multipart
    @PUT("profile/edit-profile")
    fun editUserProfil(
        @Header("Authorization") authorization: String,
        @Part("nama") nama: String,
        @Part photo: MultipartBody.Part?,
    ): Call<EditProfilResponse>

    @PUT("profile/edit-pass")
    fun EditPassword(
        @Header("Authorization") authorization: String,
        @Body data: EditPassword,
    ): Call<EditPasswordResponse>

    @GET("forum")
    fun getForum(
        @Header("Authorization") authorization: String
    ): Call<ForumItemsResponse>

    @GET("lms")
    fun getAllLesson(
        @Header("Authorization") authorization: String,
    ): Call<lessonAllResponse>

    @GET("lms/find")
    fun searchLesson(
        @Header("Authorization") authorization: String,
        @Body data: searchdatalms,
    ): Call<searchResponse>

    @GET("forum/{id}")
    fun getDetailForum(
        @Path("id") forumId: Int,
        @Header("Authorization") authorization: String
    ): Call<ForumByIdResponse>

    @POST("forum/{id}/comment")
    fun postComment(
        @Path("id") forumId: Int,
        @Header("Authorization") authorization: String,
        @Body data: CommentData
    ): Call<CommentResponse>

    

}