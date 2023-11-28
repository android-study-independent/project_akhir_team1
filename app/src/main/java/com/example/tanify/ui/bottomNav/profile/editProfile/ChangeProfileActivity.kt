package com.example.tanify.ui.bottomNav.profile.editProfile

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.util.Log
import android.view.View
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.example.tanify.data.api.tanify.ApiConfig
import com.example.tanify.data.response.EditProfilResponse
import com.example.tanify.data.response.UserProfilResponse
import com.example.tanify.databinding.ActivityChangeProfileBinding
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ChangeProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChangeProfileBinding
    private lateinit var sharedPreferences: SharedPreferences
    private var imgprofil : Uri?= null
    private  var dataprofil: UserProfilResponse ?= null
    companion object {
        private const val TAG = "ChangeProfil"
        private const val PREF_NAME = "MyAppPreferences"
        private var TOKEN = "token"
        private var PROFIL = "profil"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangeProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ambil data login, profil, dan imgprofil dari SharedPreferences
        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        TOKEN = sharedPreferences.getString("token", "").toString()
        PROFIL = sharedPreferences.getString("profil", null).toString()

        // Ambil URI gambar dari SharedPreferences
        val imgUriString = sharedPreferences.getString("imageProfil", null)
        imgprofil = if (imgUriString != null) Uri.parse(imgUriString) else null

        val gson = Gson()
        dataprofil = gson.fromJson(PROFIL, UserProfilResponse::class.java)
        Log.d("token", TOKEN)

        setdataprofil(dataprofil)
        startActivity()
    }
    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            imgprofil = uri
            imgprofil?.let { binding.imgprofil.setImageURI(it)}
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }
    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun setdataprofil(dataprofil: UserProfilResponse?) {
        binding.iptNama.text = Editable.Factory.getInstance().newEditable(dataprofil?.nama)
        binding.iptEmail.text = Editable.Factory.getInstance().newEditable(dataprofil?.email)
        imgprofil?.let { binding.imgprofil.setImageURI(it) }
    }

    private fun prepareImagePart(uri: Uri): MultipartBody.Part {
        val file = File(uri.path ?: "")
        val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
        return MultipartBody.Part.createFormData("photo", file.name, requestFile)
    }

    private fun startActivity() {
        binding.btnBack.setOnClickListener{
            finish();
        }
        binding.btnBatalkan.setOnClickListener{
            finish();
        }
        binding.iptimg.setOnClickListener{
            Log.d("========= edit profil","================================= get img")
            startGallery()
        }
        binding.btnSimpan.setOnClickListener{
            val nama = binding.iptNama.text.toString()
            // simpan ke api
            Log.d("berhasil", "nama -=================----------------================ $nama")
            val imgprofilPart = imgprofil?.let { prepareImagePart(it) }
            ApiConfig.instanceRetrofit.editUserProfil(
                "Bearer " + TOKEN,
                nama,
                imgprofilPart ?: MultipartBody.Part.createFormData("photo", "")
            ).enqueue(object : Callback<EditProfilResponse>{
                override fun onResponse(
                    call: Call<EditProfilResponse>,
                    response: Response<EditProfilResponse>,
                ) {
                    Log.d("cek berhasil ", "================================ 0 ===========")
                    if (response.isSuccessful) {
                        // jika regis berhasil, maka ada parameter msg
                        Log.d("berasil ", "================================ 1")
                        Log.d("berasil ", response.body().toString())
                        Log.d("berasil ", "================================ 1")
                        showSnackbar(response.body()?.msg.toString())
                    } else {
                        Log.d("gagal regis ", "================================ 901")
                        Log.e(TAG, "onFailure: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<EditProfilResponse>, t: Throwable) {
                    Log.d("gagal regis ", "================================ 902")
                    Log.e(TAG, "onFailure (OF): ${t.message.toString()}")
                }

            })


        }
    }
    private fun showSnackbar(message: String) {
        val rootView: View = findViewById(android.R.id.content)
        val snackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT)
        snackbar.show()
    }

}