package com.example.tanify.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.tanify.R
import com.example.tanify.data.api.ApiConfig
import com.example.tanify.data.response.LoginResponse
import com.example.tanify.databinding.ActivityLoginBinding
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    companion object {
        private const val TAG = "LoginActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setAction()
    }

    private fun setAction() {
        binding.btnMasuk.setOnClickListener {
            val email = binding.edEmail.text.toString()
            val password = binding.edPassword.text.toString()

            when {
                email.isEmpty() -> {
                    binding.edEmail.error = "Email tidak boleh kosong!"
                    binding.edEmail.requestFocus()
                    return@setOnClickListener
                }

                password.isEmpty() -> {
                    binding.edPassword.error = "Password tidak boleh kosong!"
                    binding.edPassword.requestFocus()
                    return@setOnClickListener
                }

                (email.isNotEmpty() && password.isNotEmpty()) -> postLogin(email, password)
            }
        }
    }

    private fun postLogin(email: String, password: String) {
        ApiConfig.instanceRetrofit.postLogin(
            email, password
        ).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val accessToken = response.body()?.token
                    val message = response.body()?.message
                    if (accessToken.isNullOrEmpty() && message.isNullOrEmpty()) {
                        Toast.makeText(this@LoginActivity, message, Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@LoginActivity, "Data tidak ada", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e(TAG, "onFailure (OF): ${t.message.toString()}")
            }
        })
    }
}