package com.example.tanify.ui.bottomNav.profile.editProfile

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.Resources
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.tanify.R
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
        private const val REQUEST_CODE_PERMISSION = 123
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangeProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ambil data login, profil, dan imgprofil dari SharedPreferences
        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        TOKEN = sharedPreferences.getString("token", "").toString()

        val gson = Gson()
        val profil = sharedPreferences.getString("profil", null).toString()
        dataprofil = gson.fromJson(profil, UserProfilResponse::class.java)
        Log.d("token", TOKEN)

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Jika izin sudah diberikan, lanjutkan dengan menginisialisasi tampilan dan memulai aktivitas
            setdataprofil(dataprofil)
            startActivity()
        } else {
            // Jika izin belum diberikan, minta izin kepada pengguna
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_CODE_PERMISSION
            )
        }

        setdataprofil(dataprofil)
        startActivity()
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Jika izin diberikan, lanjutkan dengan menginisialisasi tampilan dan memulai aktivitas
                setdataprofil(dataprofil)
                startActivity()
            } else {
                // Jika izin ditolak, berikan pesan atau tindakan yang sesuai
                Log.d(TAG, "Permission denied")
                // Tambahkan kode di sini untuk memberikan pesan kepada pengguna atau melakukan tindakan lainnya
            }
        }
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            imgprofil = uri
            imgprofil?.let { binding.imgprofil.setImageURI(it)}
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun setdataprofil(dataprofil: UserProfilResponse?) {
        binding.iptNama.text = Editable.Factory.getInstance().newEditable(dataprofil?.nama)
        binding.iptEmail.text = Editable.Factory.getInstance().newEditable(dataprofil?.email)
        val foto = dataprofil?.photo?.removePrefix("../")
        Glide.with(this)
            .load("http://195.35.32.179:8001/"+foto)
            .placeholder(R.drawable.icon_user)
            .error(R.drawable.icon_user)
            .into(binding.imgprofil)
    }
    private fun startGallery() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            launcherGallery.launch("image/*")
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_CODE_PERMISSION
            )
        }
    }

    private fun prepareImagePart(uri: Uri?): MultipartBody.Part {
        val contentResolver = contentResolver
        val inputStream = uri?.let { contentResolver.openInputStream(it) }
        val file = createTempFile()

        inputStream?.use { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
            }
        }

        // Mendapatkan tipe media dari URI (contoh: image/jpeg, image/png)
        val contentType = contentResolver.getType(uri!!)?.toMediaTypeOrNull()
        Toast.makeText(applicationContext, "type gambar : $contentType", Toast.LENGTH_SHORT).show()


        // Membuat bagian MultipartBody dengan tipe media yang sesuai
        val requestFile = RequestBody.create(contentType, file)
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
            Log.d("========= edit profil","========1========================= get img")
            startGallery()
        }
        binding.btnSimpan.setOnClickListener{
            val nama = binding.iptNama.text.toString()
            // simpan ke api
            Log.d("berhasil", "nama -=================----------------================ $nama")
            val imgprofilPart = imgprofil?.let { prepareImagePart(it) }
                ?: prepareImagePart(getDefaultImageUri())
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
                        Toast.makeText(applicationContext, "berhasil", Toast.LENGTH_SHORT).show()

                    } else {
                        Log.d("gagal regis ", "================================ 901")
                        Log.e(TAG, "onFailure: ${response.message()}")
                        Toast.makeText(applicationContext, "gagal 1", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<EditProfilResponse>, t: Throwable) {
                    Log.d("gagal regis ", "================================ 902")
                    Toast.makeText(applicationContext, "gagal total", Toast.LENGTH_SHORT).show()
                }

            })


        }
    }

    private fun getDefaultImageUri(): Uri? {
        val drawableId = R.drawable.foto_profile
        val drawableUri = Resources.getSystem().getResourceName(drawableId)
        Toast.makeText(applicationContext, "gambar is null", Toast.LENGTH_SHORT).show()
        return Uri.parse("android.resource://$packageName/$drawableUri")
    }



}