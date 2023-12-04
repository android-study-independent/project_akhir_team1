package com.example.tanify.ui.lms

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tanify.R
import com.example.tanify.data.api.tanify.ApiConfig
import com.example.tanify.data.response.lms.ModulItem
import com.example.tanify.data.response.lms.lessonAllResponse
import com.example.tanify.data.response.profile.UserProfilResponse
import com.example.tanify.databinding.ActivityLmsBinding
import com.example.tanify.helper.GetUserProfilCallback
import com.example.tanify.helper.getUserProfil
import com.example.tanify.ui.lms.adapterLessson.lessonAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LmsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLmsBinding
    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var userProfil: UserProfilResponse
    private lateinit var ModulLesson: lessonAllResponse

    private lateinit var adapterLms: lessonAdapter
    private lateinit var recyclerviewRekomendasi: RecyclerView
    private lateinit var recyclerviewModul: RecyclerView


    companion object {
        private const val TAG = "lmsHome"
        private var TOKEN = "token"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLmsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // sharedPreferences data
        sharedPreferences = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        TOKEN = sharedPreferences.getString("token", "").toString()

        // Initialize recyclerviewRekomendasi
        recyclerviewRekomendasi = findViewById(R.id.rvListRekomendasi)
        recyclerviewModul = findViewById(R.id.rvYourlessons)


        startAction()
        setProfil()
        getDataAllModul()

    }

    private fun setListModul() {
        if (::ModulLesson.isInitialized) {
            val dataset: List<ModulItem> = ModulLesson.modul
            adapterLms = lessonAdapter(dataset, 2)
            recyclerviewModul.adapter = adapterLms
            val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            recyclerviewModul.layoutManager = layoutManager
        } else {
            Log.e(TAG, "ModulLesson or AllLesson is not initialized")
        }

    }

    private fun setRekomendasiModul() {
        // ambil data rekomendarumodul, dan update ke rvListRekomendasi
        if (::ModulLesson.isInitialized) {
            val dataset: List<ModulItem> = ModulLesson.rekomendasi
            adapterLms = lessonAdapter(dataset, 1)
            recyclerviewRekomendasi.adapter = adapterLms
            val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            recyclerviewModul.layoutManager = layoutManager
        } else {
            Log.e(TAG, "ModulLesson or rekomendasi is not initialized")
        }


    }

    private fun getDataAllModul() {
        val tag = "Get All Modul :"
        ApiConfig.instanceRetrofit.getAllLesson("Bearer $TOKEN")
            .enqueue(object : Callback<lessonAllResponse> {
                override fun onResponse(
                    call: Call<lessonAllResponse>,
                    response: Response<lessonAllResponse>,
                ) {
                    if (response.isSuccessful) {
                        ModulLesson = response.body()!!
                        Log.d(tag, ModulLesson.msg)

                        setRekomendasiModul()
                         setListModul()

                        Log.d("tes tes :", "================================================== 4")
                    }
                }

                override fun onFailure(call: Call<lessonAllResponse>, t: Throwable) {
                    Log.e(tag, "onFailure: ${t.message.toString()}")
                }

            })
    }

    private fun setProfil() {
        getUserProfil(TOKEN, object : GetUserProfilCallback {
            override fun onUserProfileReceived(userProfil: UserProfilResponse) {
                binding.tvHalloUser.text = userProfil.nama
                val foto = userProfil.photo?.removePrefix("../")
                Glide.with(this@LmsActivity)
                    .load("http://195.35.32.179:8001/" + foto)
                    .skipMemoryCache(false)
                    .placeholder(R.drawable.icon_user)
                    .error(R.drawable.icon_user)
                    .into(binding.ivUserlms)
            }

            override fun onFailed(message: String) {
                Log.e(TAG, "get Profile: ${message}")
            }
        })
    }

    private fun startAction() {
        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnsearchLesson.setOnClickListener {
            val textSearch = binding.editTextSearch.text.toString()
            //intent laman baru search
        }
    }

}