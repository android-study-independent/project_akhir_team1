package com.example.tanify.ui.bottomNav.profile.editProfile

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.tanify.R
import com.example.tanify.data.api.tanify.ApiConfig
import com.example.tanify.data.data.EditPassword
import com.example.tanify.data.response.EditPasswordResponse
import com.example.tanify.databinding.ActivityChangePasswordBinding
import com.example.tanify.databinding.ActivityChangeProfileBinding
import com.example.tanify.ui.bottomNav.profile.ProfileFragment
import com.example.tanify.ui.register.RegisterActivity
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangePasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChangePasswordBinding
    private lateinit var sharedPreferences: SharedPreferences
    companion object {
        private const val TAG = "ChangePassword"
        private const val PREF_NAME = "MyAppPreferences"
        private var TOKEN = "token"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //ambil data login
        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        TOKEN = sharedPreferences.getString("token", "").toString()
        Log.d("token", TOKEN)

        startActivity()


    }
    

    private fun startActivity() {
        binding.btnBack.setOnClickListener{
            finish();
        }

        binding.btnBatalkan.setOnClickListener{
            finish();
        }
        binding.btnSimpan.setOnClickListener{
            val pwl = binding.iptPasswordlama.text.toString()
            val pwb = binding.iptPasswordBaru.text.toString()
            val pwk = binding.iptKonfirmasiPassword.text.toString()
            
            setEditPassword(pwl, pwb, pwk)
        }


    }

    private fun setEditPassword(pwl: String, pwb: String, pwk: String) {
        val data = EditPassword(pwl, pwb, pwk)
        ApiConfig.instanceRetrofit.EditPassword(
            "Bearer " + TOKEN,data
        ).enqueue(object :Callback<EditPasswordResponse>{
            override fun onResponse(
                call: Call<EditPasswordResponse>,
                response: Response<EditPasswordResponse>,
            ) {
                if (response.isSuccessful) {
                    Log.d("berasil ", "================================ 1")
                    if(response.body()?.message != null){
                        val message = response.body()?.message
                        showSnackbar(message.toString())
                        Handler().postDelayed({
                            finish()
                        }, 2000)
                    }
                }else{
                    Log.d("gagal ", "================================ 2")
                    if (response.code() == 400) {
                        try {
                            val errorResponse: EditPasswordResponse? = Gson().fromJson(response.errorBody()?.charStream(), EditPasswordResponse::class.java)
                            if (errorResponse != null) {
                                val error = errorResponse.error
                                when{
                                    (error?.oldPassword != null)->{
                                        showSnackbar(error.oldPassword.msg.toString())
                                        binding.iptPasswordlama.requestFocus()
                                    }
                                    (error?.newPassword != null)->{
                                        showSnackbar(error.newPassword.msg.toString())
                                        binding.iptPasswordBaru.requestFocus()
                                    }
                                    (error?.confirmPassword != null)->{
                                        showSnackbar(error.confirmPassword.msg.toString())
                                        binding.iptKonfirmasiPassword.requestFocus()
                                    }
                                }
                            } else {
                                // Handle jika parsing respons gagal
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            // Handle kesalahan saat parsing JSON
                        }
                    }
                }
            }
            override fun onFailure(call: Call<EditPasswordResponse>, t: Throwable) {
                Log.e(TAG, "onFailure (OF): ${t.message.toString()}")
            }

        })
    }
    private fun showSnackbar(message: String) {
        val rootView: View = findViewById(android.R.id.content)
        val snackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT)
        snackbar.show()
    }
}