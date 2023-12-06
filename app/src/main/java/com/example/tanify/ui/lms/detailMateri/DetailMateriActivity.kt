package com.example.tanify.ui.lms.detailMateri

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.tanify.R
import com.example.tanify.data.api.tanify.ApiConfig
import com.example.tanify.data.data.putProgres
import com.example.tanify.data.response.lms.SectionItem
import com.example.tanify.data.response.lms.SectionyIdResponse
import com.example.tanify.data.response.lms.dataPutProgres
import com.example.tanify.data.response.lms.putProgresResponse
import com.example.tanify.databinding.ActivityCheckListMateriBinding
import com.example.tanify.databinding.ActivityDetailMateriBinding
import com.example.tanify.ui.lms.LmsActivity
import com.example.tanify.ui.lms.checklistMateri.CheckListMateriActivity
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailMateriActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailMateriBinding
    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var dataSection: SectionyIdResponse

    companion object {
        private const val TAG = "DetailMateri"
        private var TOKEN = "token"
        private var IDModul = 0
        private var IDSection = 0

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailMateriBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // sharedPreferences data
        sharedPreferences = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        TOKEN = sharedPreferences.getString("token", "").toString()

        // get id
        IDModul = intent.getIntExtra("idModul", 0)
        IDSection = intent.getIntExtra("idSection", 0)

        Log.d("dapat data :", IDModul.toString() + " : " + IDSection)

        // mulai apk
        getdata()
        startAction()
    }

    private fun startAction() {
        binding.btnBack.setOnClickListener {
            finish()
        }
        binding.btnselesai.setOnClickListener {
            // kirim put
            Log.d("tess", "================================== 1")
            ApiConfig.instanceRetrofit.putProgres(
                "Bearer ${TOKEN}",
                IDModul.toString(),
                IDSection.toString(),
                putProgres(true)
            ).enqueue(object : Callback<putProgresResponse> {
                override fun onResponse(
                    call: Call<putProgresResponse>,
                    response: Response<putProgresResponse>
                ) {
                    Log.d("tess", "================================== 1")
                    if (response.isSuccessful) {
                        Toast.makeText(applicationContext, response.body()?.msg, Toast.LENGTH_SHORT)
                            .show()
                        finish()
                    } else {
                        when (response.code()) {
                            404 -> {
                                Toast.makeText(
                                    applicationContext,
                                    response.body()?.msg,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                            else -> {
                                Log.e("Error", "Unexpected response code: ${response.code()}")
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<putProgresResponse>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
        }
    }

    private fun getdata() {
        val tag = "Get All Section :"
        ApiConfig.instanceRetrofit.getSectionById(
            "Bearer ${TOKEN}",
            IDModul.toString(),
            IDSection.toString()
        ).enqueue(object : Callback<SectionyIdResponse> {
            override fun onResponse(
                call: Call<SectionyIdResponse>,
                response: Response<SectionyIdResponse>
            ) {
                if (response.isSuccessful) {
                    dataSection = response.body()!!
                    Log.d(tag, dataSection.msg)

                    setdata(dataSection.data)
                }
            }

            override fun onFailure(call: Call<SectionyIdResponse>, t: Throwable) {
                Log.e(tag, "onFailure: ${t.message.toString()}")
            }

        })
    }

    private fun setdata(data: SectionItem) {
        // Mengatur sumber video dari URL
        val linkyt = data.cover // ambil id YouTube

        // Ambil video ID dari URL menggunakan Uri
        val videoUri = Uri.parse(linkyt)
        val videoId = videoUri.pathSegments[0]

        Log.d("link Youtube", videoId)

        // Mengatur video YouTube
        binding.youtubePlayerView.addYouTubePlayerListener(object :
            AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                youTubePlayer.cueVideo(videoId, 0f) // Perbaikan nilai dari Of menjadi 0f
            }
        })


        binding.titleSection.text = data.section
        binding.textSection.loadData(
            data.content.toString(),
            "text/html",
            "UTF-8"
        )
    }

}