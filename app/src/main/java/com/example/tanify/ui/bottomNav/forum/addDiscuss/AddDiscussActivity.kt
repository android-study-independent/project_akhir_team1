package com.example.tanify.ui.bottomNav.forum.addDiscuss

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.tanify.R
import com.example.tanify.data.api.tanify.ApiConfig
import com.example.tanify.data.data.NewForumData
import com.example.tanify.data.response.forum.AddDiscussResponse
import com.example.tanify.databinding.ActivityAddDiscussBinding
import com.example.tanify.ui.bottomNav.forum.detailDiscuss.DetailDiscussActivity
import com.example.tanify.ui.bottomNav.profile.editProfile.ChangeProfileActivity
import com.google.android.material.snackbar.Snackbar
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

@Suppress("UNREACHABLE_CODE")
class AddDiscussActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddDiscussBinding
    private lateinit var sharedPreferences: SharedPreferences
    private var currentImgPoster: Uri? = null

    companion object {
        private const val TAG = "AddDiscussActivity"
        private const val PREF_NAME = "MyAppPreferences"
        private var TOKEN = "token"
        private const val REQUEST_CODE_PERMISSION = 123
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddDiscussBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = this.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        TOKEN = sharedPreferences.getString("token", "").toString()

        checkStoragePermission()
        setAction()
    }

    private fun setAction(){
        binding.btnAddImg.setOnClickListener {
            startGalery()
        }
        binding.btnPost.setOnClickListener {
            val title = binding.edTitle.text.toString()
            val content = binding.edContent.text.toString()
            if (currentImgPoster != null) {
                var imgPosterPart: MultipartBody.Part? = null
                currentImgPoster?.let { uri ->
                    val imageFile = uriToFile(uri, this)
                    Log.d("Image File", "showImage: ${imageFile.path}")

                    val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
                    imgPosterPart = MultipartBody.Part.createFormData(
                        "photo",
                        imageFile.name,
                        requestImageFile
                    )
                } ?: {
                    imgPosterPart = null
                }
                postNewDiscuss(title, content, imgPosterPart!!)
                finish()

            } else {
                showSnackbar("Harap masukkan gambar")
            }
        }
    }

    private fun postNewDiscuss(title: String, content: String, cover: MultipartBody.Part){
        val dataForum = NewForumData(title, content, cover)
        ApiConfig.instanceRetrofit.postNewForum(
            dataForum,
            "Bearer " + TOKEN
        ).enqueue(object : Callback<AddDiscussResponse>{
            override fun onResponse(
                call: Call<AddDiscussResponse>,
                response: Response<AddDiscussResponse>
            ) {
                if (response.isSuccessful) {
                    val newPostResponse = response.body()
                    if (newPostResponse != null) {
                        Log.d(TAG, "onSuccess: ${newPostResponse.msg}")
                        showSnackbar("{$newPostResponse.msg}")
                    }
                }
            }

            override fun onFailure(call: Call<AddDiscussResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun startGalery(){
        if (isPhotoPickerAvailable(this)){
            showSnackbar("Device tidak mendukung")
        } else {
            launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    private fun isPhotoPickerAvailable(context: Context): Boolean {
        val photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"

        val packageManager = context.packageManager
        val activities = packageManager.queryIntentActivities(photoPickerIntent, 0)

        return activities.isNotEmpty()
    }


    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImgPoster = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun showImage() {
        currentImgPoster?.let {
            Log.d("Image URI", "showImg: $it")
            binding.ivPreview.setImageURI(it)
        }
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

    private fun showSnackbar(message: String) {
        val rootView: View = findViewById(android.R.id.content)
        val snackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT)
        snackbar.show()
    }

    private fun uriToFile(imageUri: Uri, context: Context): File {
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

    private fun createCustomTempFile(context: Context): File {
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("profil_user", ".jpg", storageDir)
    }
}