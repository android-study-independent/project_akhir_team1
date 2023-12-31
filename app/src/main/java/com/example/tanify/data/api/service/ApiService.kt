package com.example.tanify.data.api.service

import com.example.tanify.data.data.CommentData
import com.example.tanify.data.data.EditPassword
import com.example.tanify.data.data.LikeForumData
import com.example.tanify.data.data.LoginData
import com.example.tanify.data.data.RegisterData
import com.example.tanify.data.data.putProgres
import com.example.tanify.data.response.profile.EditPasswordResponse
import com.example.tanify.data.response.profile.EditProfilResponse
import okhttp3.MultipartBody
import com.example.tanify.data.response.artikel.ArtikelResponse
import com.example.tanify.data.response.forum.AddDiscussErrorResponse
import com.example.tanify.data.response.lms.ProgresResponse
import com.example.tanify.data.response.lms.SectionyIdResponse
import com.example.tanify.data.response.forum.CommentResponse
import com.example.tanify.data.response.forum.ForumByIdResponse
import com.example.tanify.data.response.forum.ForumItemsResponse
import com.example.tanify.data.response.forum.LikeForumResponse
import com.example.tanify.data.response.lms.dataPutProgres
import com.example.tanify.data.response.lms.lessonAllResponse
import com.example.tanify.data.response.lms.lessonByIdResponse
import com.example.tanify.data.response.lms.putProgresResponse
import com.example.tanify.data.response.lms.searchResponse
import com.example.tanify.data.response.weather.CurrentWeatherResponse
import com.example.tanify.data.response.login.LoginResponse
import com.example.tanify.data.response.login.RegisterRespons
import com.example.tanify.data.response.profile.UserProfilResponse
import com.example.tanify.data.response.weather.WeeklyWeatherResponseItem
import okhttp3.RequestBody
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
        @Query("search") search: String
    ): Call<searchResponse>

    @GET("lms/lesson/{id}")
    fun getLessonById(
        @Header("Authorization") authorization: String,
        @Path("id") id: String
    ): Call<lessonByIdResponse>

    @GET("lms/lesson/{id_lesson}/section/{id_section}")
    fun getSectionById(
        @Header("Authorization") authorization: String,
        @Path("id_lesson") id_lesson: String,
        @Path("id_section") id_section: String
    ): Call<SectionyIdResponse>

    @GET("lms/progres")
    fun getProgres(
        @Header("Authorization") authorization: String,
    ): Call<ProgresResponse>


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

    @PUT("lms/lesson/{id_lesson}/section/{id_section}")
    fun putProgres(
        @Header("Authorization") authorization: String,
        @Path("id_lesson") id_lesson: String,
        @Path("id_section") id_section: String,
        @Body data: putProgres
    ): Call<putProgresResponse>

    @GET("forum/posts/find")
    fun getSearchForum(
        @Header("Authorization") authorization: String,
        @Query("search") search: String
    ): Call<ForumItemsResponse>

    @POST("forum/new-post")
    @Multipart
    fun postNewForum(
        @Header("Authorization") authorization: String,
        @Part("title") title: RequestBody,
        @Part("content") content: RequestBody,
        @Part cover: MultipartBody.Part?
    ): Call<AddDiscussErrorResponse>

    @POST("forum/{id}/like")
    fun postLikeForum(
        @Header("Authorization") authorization: String,
        @Path("id") id: String,
        @Body data: LikeForumData
    ): Call<LikeForumResponse>

}