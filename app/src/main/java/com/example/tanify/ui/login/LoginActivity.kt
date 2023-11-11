package com.example.tanify.ui.login

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.example.tanify.data.api.ApiConfig
import com.example.tanify.data.data.LoginData
import com.example.tanify.data.response.LoginResponse
import com.example.tanify.databinding.ActivityLoginBinding
import com.google.android.material.snackbar.Snackbar
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
                    showSnackbar(message.toString())
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e(TAG, "onFailure (OF): ${t.message.toString()}")
                showLoading(false)
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

    private fun showLoading(isLoading: Boolean){
        if (isLoading) {
            binding.progressCircular.visibility = View.VISIBLE
        } else {
            binding.progressCircular.visibility = View.GONE
        }
    }
}