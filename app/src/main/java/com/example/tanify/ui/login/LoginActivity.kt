package com.example.tanify.ui.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.example.tanify.data.api.tanify.ApiConfig
import com.example.tanify.data.data.LoginData
import com.example.tanify.data.response.login.LoginResponse
import com.example.tanify.databinding.ActivityLoginBinding
import com.example.tanify.ui.MainActivity
import com.example.tanify.ui.register.RegisterActivity
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var sharedPref: SharedPreferences

    companion object {
        private const val TAG = "LoginActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)

        setAction()
    }

    private fun setAction() {
        binding.btnMasuk.setOnClickListener {
            val email = binding.edEmail.text.toString()
            val password = binding.edPassword.text.toString()
            hideKeyboard()

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

        binding.btnDaftar.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun postLogin(email: String, password: String) {
        val data = LoginData(email, password)
        showLoading(true)
        ApiConfig.instanceRetrofit.postLogin(
            data
        ).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                showLoading(false)
                if (response.isSuccessful) {
                    val accessToken = response.body()?.token
                    val message = response.body()?.message
                    Log.d(TAG, "onSuccess: $message")
                    Log.d(TAG, "onSuccess: $accessToken")
                    handledLogginSuccess(accessToken.toString())
                } else {
                    showSnackbar("Email atau kata sandi invalid")
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e(TAG, "onFailure (OF): ${t.message.toString()}")
                showLoading(false)
                showSnackbar("Gagal memuat data")
            }
        })
    }

    private fun showSnackbar(message: String) {
        val rootView: View = findViewById(android.R.id.content)
        val snackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT)
        snackbar.show()
    }

    private fun hideKeyboard() {
        val view: View? = currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun handledLogginSuccess(token: String) {
        with(sharedPref.edit()) {
            putString("token", token)
            apply()
        }
        showSnackbar("Login berhasil")
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showLoading(isLoading: Boolean){
        if (isLoading) {
            binding.progressCircular.visibility = View.VISIBLE
        } else {
            binding.progressCircular.visibility = View.GONE
        }
    }
}