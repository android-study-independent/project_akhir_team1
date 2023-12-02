package com.example.tanify.ui.bottomNav.profile.editProfile

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.text.Editable
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.tanify.R
import com.example.tanify.data.api.tanify.ApiConfig
import com.example.tanify.data.response.EditProfilResponse
import com.example.tanify.data.response.profile.UserProfilResponse
import com.example.tanify.databinding.ActivityChangeProfileBinding
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class ChangeProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChangeProfileBinding
    private lateinit var sharedPreferences: SharedPreferences
    private var imgprofil : Uri?= null
    private  var dataprofil: UserProfilResponse?= null
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

        checkStoragePermission()


        setdataprofil(dataprofil)
        startActivity()
    }
    private fun checkStoragePermission(): Boolean {
        return if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_CODE_PERMISSION
            )
            return false
        }
    }


    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            imgprofil = uri
            imgprofil?.let { binding.imgprofil.setImageURI(it)}
            Log.d("Photo Picker", imgprofil.toString())
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun setdataprofil(dataprofil: UserProfilResponse?) {
        binding.iptNama.text = Editable.Factory.getInstance().newEditable(dataprofil?.nama?.replace("[\\\"]".toRegex(), ""))
        binding.iptEmail.text = Editable.Factory.getInstance().newEditable(dataprofil?.email)
        val foto = dataprofil?.photo?.removePrefix("../")
//        Glide.with(this)
//            .load("http://195.35.32.179:8001/"+foto)
//            .diskCacheStrategy(DiskCacheStrategy.NONE)
//            .placeholder(R.drawable.icon_user)
//            .error(R.drawable.icon_user)
//            .into(binding.imgprofil)
    }
    

    private fun startActivity() {
        binding.btnBack.setOnClickListener{
            finish()
        }
        binding.btnBatalkan.setOnClickListener{
            finish()
        }
        binding.iptimg.setOnClickListener{
            Log.d("========= edit profil","========1========================= get img")
            if(checkStoragePermission()){
                launcherGallery.launch("image/*")
            }
        }
        binding.btnSimpan.setOnClickListener{
            val nama = binding.iptNama.text.toString().trim()
            // simpan ke api
            Log.d("berhasil", "nama -=================----------------================ $nama")

            var imgprofilPart: MultipartBody.Part? = null

            imgprofil?.let{uri ->
                val imageFile = uriToFile(uri, this)
                Log.d("Image File", "showImage: ${imageFile.path}")

                val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
                imgprofilPart = MultipartBody.Part.createFormData(
                    "photo",
                    imageFile.name,
                    requestImageFile
                )
            }?:{
                imgprofilPart = null
            }

            ApiConfig.instanceRetrofit.editUserProfil(
                "Bearer " + TOKEN,
                nama,
                imgprofilPart
            ).enqueue(object : Callback<EditProfilResponse>{
                override fun onResponse(
                    call: Call<EditProfilResponse>,
                    response: Response<EditProfilResponse>,
                ) {
                    Log.d("cek berhasil ", "================================ 0 ===========")
                    if (response.isSuccessful) {
                        Log.d("berhasil regis ", "================================ 100")
                        Toast.makeText(applicationContext, "berhasil", Toast.LENGTH_SHORT).show()
                        Handler().postDelayed({
                            finish()
                        }, 1000)
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

    fun uriToFile(imageUri: Uri, context: Context): File {
        val myFile = createCustomTempFile(context)
        val inputStream = context.contentResolver.openInputStream(imageUri) as InputStream
        val outputStream = FileOutputStream(myFile)
        val buffer = ByteArray(1024)
        var length: Int
        while (inputStream.read(buffer).also { length = it } > 0) outputStream.write(buffer, 0, length)
        outputStream.close()
        inputStream.close()
        return myFile
    }

    fun createCustomTempFile(context: Context): File {
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("profil_user", ".jpg", storageDir)
    }









}