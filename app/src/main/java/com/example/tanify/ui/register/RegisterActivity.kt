package com.example.tanify.ui.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import com.example.tanify.data.api.ApiConfig
import com.example.tanify.data.data.RegisterData
import com.example.tanify.data.response.RegisterRespons
import com.example.tanify.databinding.ActivityRegisterBinding
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
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
        showLoading(true)
        ApiConfig.instanceRetrofit.postRegister(data)
            .enqueue(object : Callback<RegisterRespons>{
                override fun onResponse(
                    call: Call<RegisterRespons>,
                    response: Response<RegisterRespons>,
                ) {
                    showLoading(false)
                    if (response.isSuccessful) {
                        // jika regis berhasil, maka ada parameter msg
                        Log.d("berasil ", "================================ 1")
                        if(response.body()?.message != null){
                            val message = response.body()?.message
                            Log.d(TAG, "onSuccess: $message")
                            showSnackbar(message.toString())
                            Handler().postDelayed({
                                finish()
                            }, 2000)
                        }
                    } else {
                        Log.d("gagal regis ", "================================ 1")
                        if (response.code() == 400) {
                            try {
                                val errorResponse: RegisterRespons? = Gson().fromJson(response.errorBody()?.charStream(), RegisterRespons::class.java)
                                if (errorResponse != null) {
                                    val error = errorResponse.error
                                    when{
                                        (error?.nama != null)->{
                                            showSnackbar(error.nama.msg.toString())
                                            binding.edRegisterNama.requestFocus()
                                        }
                                        (error?.email != null)->{
                                            showSnackbar(error.email.msg.toString())
                                            binding.edRegisterEmail.requestFocus()
                                        }
                                        (error?.password != null)->{
                                            showSnackbar(error.password.msg.toString())
                                            binding.edRegisterPassword.requestFocus()
                                        }
                                        (error?.konfirmasiPassword != null)->{
                                            showSnackbar(error.konfirmasiPassword.msg.toString())
                                            binding.edRegisterConfirmPassword.requestFocus()
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
                        Log.e(TAG, "onFailure: ${response.message()}")
                    }
                }
                override fun onFailure(call: Call<RegisterRespons>, t: Throwable) {
                    Log.e(TAG, "onFailure (OF): ${t.message.toString()}")
                }
            })
    }
    private fun showSnackbar(message: String) {
        val rootView: View = findViewById(android.R.id.content)
        val snackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT)
        snackbar.show()
    }
    private fun showLoading(isLoading: Boolean){
        if (isLoading) {
            binding.progressCircular.visibility = View.VISIBLE
        } else {
            binding.progressCircular.visibility = View.GONE
        }
    }


}