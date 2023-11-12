package com.example.tanify.ui.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.tanify.data.api.ApiConfig
import com.example.tanify.data.data.RegisterData
import com.example.tanify.data.response.RegisterRespons
import com.example.tanify.databinding.ActivityRegisterBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    companion object {
        private const val TAG = "Register Activity"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setAction()
    }

    private fun setAction() {
        binding.btnLogin.setOnClickListener{
            finish()
        }
        binding.btnDaftar.setOnClickListener{
            val nama = binding.edRegisterNama.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()
            val cpassword = binding.edRegisterConfirmPassword.text.toString()
            Log.d("daftar", "================================ 1 ================================")

            when {
                nama.isEmpty() -> {
                    binding.edRegisterNama.error = "Email tidak boleh kosong!"
                    binding.edRegisterNama.requestFocus()
                    return@setOnClickListener
                }

                email.isEmpty() -> {
                    binding.edRegisterEmail.error = "Email tidak boleh kosong!"
                    binding.edRegisterEmail.requestFocus()
                    return@setOnClickListener
                }

                password.isEmpty() -> {
                    binding.edRegisterPassword.error = "Password tidak boleh kosong!"
                    binding.edRegisterPassword.requestFocus()
                    return@setOnClickListener
                }

                cpassword.isEmpty() -> {
                    binding.edRegisterConfirmPassword.error = "Konfrimasi password tidak boleh kosong!"
                    binding.edRegisterConfirmPassword.requestFocus()
                    return@setOnClickListener
                }


                (nama.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && cpassword.isNotEmpty()) -> actiondaftar(nama, email, password, cpassword)
            }

        }
    }

    private fun actiondaftar(nama: String, email: String, password: String, cpassword: String) {
        val data = RegisterData(nama, email, password, cpassword)
        // loading
        Log.d("daftar", "================================ 2 ================================")
        ApiConfig.instanceRetrofit.postRegister(data)
            .enqueue(object : Callback<RegisterRespons>{
                override fun onResponse(
                    call: Call<RegisterRespons>,
                    response: Response<RegisterRespons>,
                ) {
                    if (response.isSuccessful) {
                        val message = response.body()?.message
                        Log.d(TAG, "onSuccess: $message")
                        Log.d("tasim", "berhasil : ============================ ")
                    } else {
                        Log.e(TAG, "onFailure: ${response.message()}")
                    }
                }
                override fun onFailure(call: Call<RegisterRespons>, t: Throwable) {
                    Log.e(TAG, "onFailure (OF): ${t.message.toString()}")
                }
            })
    }

}