package com.example.tanify.ui.lms

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tanify.R
import com.example.tanify.data.api.tanify.ApiConfig
import com.example.tanify.data.response.lms.LmsDto
import com.example.tanify.data.response.lms.ModulItem
import com.example.tanify.data.response.lms.ProgresResponse
import com.example.tanify.data.response.lms.lessonAllResponse
import com.example.tanify.data.response.profile.UserProfilResponse
import com.example.tanify.databinding.ActivityLmsBinding
import com.example.tanify.helper.GetUserProfilCallback
import com.example.tanify.helper.getUserProfil
import com.example.tanify.ui.bottomNav.profile.editProfile.ChangePasswordActivity
import com.example.tanify.ui.lms.ListSearchLesson.ListSearchActivity
import com.example.tanify.ui.lms.adapterLessson.lessonAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LmsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLmsBinding
    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var userProfil: UserProfilResponse
    private lateinit var ModulLesson: lessonAllResponse
    private lateinit var dataProgres: ProgresResponse

    private lateinit var adapterLms: lessonAdapter
    private lateinit var recyclerviewRekomendasi: RecyclerView
    private lateinit var recyclerviewModul: RecyclerView
    private lateinit var recyclerviewProgres: RecyclerView


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
        recyclerviewProgres = findViewById(R.id.rvListProgres)

        startAction()
        setProfil()
        getDataAllModul()

    }

    override fun onResume() {
        super.onResume()
        getProgres()
    }

    private fun getProgres() {
        val tag = "Get All Progres :"
        ApiConfig.instanceRetrofit.getProgres("Bearer $TOKEN")
            .enqueue(object : Callback<ProgresResponse> {
                override fun onResponse(
                    call: Call<ProgresResponse>,
                    response: Response<ProgresResponse>
                ) {
                    Log.d("tes tes :", "================================================== get progress")
                    if (response.isSuccessful) {
                        dataProgres = response.body()!!
                        Log.d(tag, dataProgres.msg)

                        setProgres()
                    }else {
                        when (response.code()) {
                            404 -> {
                                Log.e(tag, "Resource not found")
                            }
                            else -> {
                                Log.e(tag, "Unexpected response code: ${response.code()}")
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<ProgresResponse>, t: Throwable) {
                    Log.e(tag, "onFailure: ${t.message.toString()}")
                }


            })
    }
    private fun getDataAllModul() {
        showLoading(true)
        val tag = "Get All Modul :"
        ApiConfig.instanceRetrofit.getAllLesson("Bearer $TOKEN")
            .enqueue(object : Callback<lessonAllResponse> {
                override fun onResponse(
                    call: Call<lessonAllResponse>,
                    response: Response<lessonAllResponse>,
                ) {
                    if (response.isSuccessful) {
                        showLoading(false)
                        ModulLesson = response.body()!!
                        Log.d(tag, ModulLesson.msg)

                        setRekomendasiModul()
                        setListModul()

                        Log.d("tes tes :", "================================================== 4")
                    } else {
                        showLoading(true)
                    }
                }

                override fun onFailure(call: Call<lessonAllResponse>, t: Throwable) {
                    Log.e(tag, "onFailure: ${t.message.toString()}")
                    showLoading(true)
                }

            })
    }

    private fun setProgres() {
        binding.linearprogres.visibility = View.VISIBLE
        if (::dataProgres.isInitialized) {
            val dataset: List<ModulItem> = dataProgres.progres
            adapterLms = lessonAdapter(dataset, 3)
            recyclerviewProgres.adapter = adapterLms
            Log.d("jumlah prgres", "==================== "+dataset.size)
            val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            recyclerviewProgres.layoutManager = layoutManager
        }else {
            Log.e(TAG, "dataProgress is not initialized")
        }

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


    private fun setProfil() {
        getUserProfil(TOKEN, object : GetUserProfilCallback {
            override fun onUserProfileReceived(userProfil: UserProfilResponse) {
                binding.tvHalloUser.text = userProfil.nama
                val foto = userProfil.photo
                Glide.with(this@LmsActivity)
                    .load(foto)
                    .skipMemoryCache(false)
                    .placeholder(R.color.grey)
                    .error(R.color.grey)
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
        binding.btnLihatSemua.setOnClickListener{
            val lmsDto = LmsDto()
            val listModul = ModulLesson.modul.mapNotNull {
                LmsDto(
                    cover = it.cover,
                    createdAt = it.createdAt,
                    view = it.view,
                    section = it.section,
                    id = it.id,
                    title = it.title,
                    totalsection = it.totalsection,
                    progres = it.progres
                )
            }
            val intent = Intent(this, ListSearchActivity::class.java)
            intent.putExtra("search", false)
            intent.putParcelableArrayListExtra("data", ArrayList(listModul))
            startActivity(intent)
        }
        binding.linearSearchlms.setOnClickListener{
            val lmsDto = LmsDto()
            val listModul = ModulLesson.modul.mapNotNull {
                LmsDto(
                    cover = it.cover,
                    createdAt = it.createdAt,
                    view = it.view,
                    section = it.section,
                    id = it.id,
                    title = it.title,
                    totalsection = it.totalsection,
                    progres = it.progres
                )
            }
            val intent = Intent(this, ListSearchActivity::class.java)
            intent.putParcelableArrayListExtra("data", ArrayList(listModul))
            intent.putExtra("search", true)
            startActivity(intent)
        }
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