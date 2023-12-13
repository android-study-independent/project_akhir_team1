package com.example.tanify.ui.lms.checklistMateri

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tanify.R
import com.example.tanify.data.api.tanify.ApiConfig
import com.example.tanify.data.response.lms.SectionItem
import com.example.tanify.data.response.lms.lessonByIdResponse
import com.example.tanify.databinding.ActivityCheckListMateriBinding
import com.example.tanify.ui.lms.adapterLessson.modulAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CheckListMateriActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCheckListMateriBinding
    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var recyclerviewSection: RecyclerView
    private lateinit var adapterSection: modulAdapter
    private lateinit var ModulLesson: lessonByIdResponse

    companion object {
        private const val TAG = "CheckListMateri"
        private var TOKEN = "token"
        private var IDModul = 0

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckListMateriBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // sharedPreferences data
        sharedPreferences = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        TOKEN = sharedPreferences.getString("token", "").toString()

        // set recyclerview
        recyclerviewSection = findViewById(R.id.rv_checklist_section)

        // get id
        IDModul = intent.getIntExtra("idModul", 0)
        Log.d(TAG, "id Modul : " + IDModul)

        startAction()

    }

    override fun onResume() {
        super.onResume()
        getdata()
    }

    private fun startAction() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun getdata() {
        showLoading(true)
        val tag = TAG+" API :"
        ApiConfig.instanceRetrofit.getLessonById(
            "Bearer $TOKEN",
            IDModul.toString()
        ).enqueue(object : Callback<lessonByIdResponse> {
            override fun onResponse(
                call: Call<lessonByIdResponse>,
                response: Response<lessonByIdResponse>
            ) {
                if (response.isSuccessful) {
                    showLoading(false)
                    ModulLesson = response.body()!!
                    Log.d(tag, ModulLesson.msg)

                    setdata()
                } else {
                    Log.d(tag+" error : ", "================================================== error API")
                    showLoading(true)
                }
            }

            override fun onFailure(call: Call<lessonByIdResponse>, t: Throwable) {
                Log.e(tag, "onFailure: ${t.message.toString()}")
                showLoading(true)
            }
        })


    }

    private fun setdata() {
        binding.tvTitleChecklist.text = ModulLesson.lesson.title
        binding.tvSectionChecklist.text = ModulLesson.section.size.toString()+" Section"

        val foto = ModulLesson.lesson.cover?.removePrefix("../")
        Glide.with(this)
            .load("http://195.35.32.179:8001/" + foto)
            .placeholder(R.drawable.icon_user)
            .error(R.drawable.icon_user)
            .into(binding.ivCoverChecklist)

        setListSection(ModulLesson.section)
        Log.d("data ==== ", ModulLesson.section.toString())

    }

    private fun setListSection(section: List<SectionItem>) {
        adapterSection = modulAdapter(section, IDModul )
        recyclerviewSection.adapter = adapterSection
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerviewSection.layoutManager = layoutManager
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.bodyLine.isGone = true
            binding.shimmerView.isVisible = true
        } else {
            binding.bodyLine.isVisible = true
            binding.shimmerView.isGone = true
        }
    }
}