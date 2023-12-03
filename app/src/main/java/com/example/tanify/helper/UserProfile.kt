package com.example.tanify.helper

import android.content.Context
import android.util.Log
import com.example.tanify.data.api.tanify.ApiConfig
import com.example.tanify.data.response.profile.UserProfilResponse
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

fun getUserProfil(token: String, callback: GetUserProfilCallback) {
    val tag = "Get User Profile"
    ApiConfig.instanceRetrofit.getUserProfil("Bearer $token")
        .enqueue(object : Callback<UserProfilResponse> {
            override fun onResponse(
                call: Call<UserProfilResponse>,
                response: Response<UserProfilResponse>,
            ) {
                if (response.isSuccessful) {
                    var dataprofil = response.body()
                    Log.d("User Profile, Name : ", dataprofil?.nama.toString())
                    if (dataprofil != null) {
                        dataprofil.nama = dataprofil.nama?.replace("[\\\"]".toRegex(), "")
                        callback.onUserProfileReceived(dataprofil)
                    }
                } else {
                    callback.onFailed(response.message())
                    Log.e(tag, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserProfilResponse>, t: Throwable) {
                callback.onFailed(t.message.toString())
                Log.e(tag, "onFailure (OF): ${t.message.toString()}")
            }
        })
}

interface GetUserProfilCallback {
    fun onUserProfileReceived(userProfil: UserProfilResponse)
    fun onFailed(message: String)
}

