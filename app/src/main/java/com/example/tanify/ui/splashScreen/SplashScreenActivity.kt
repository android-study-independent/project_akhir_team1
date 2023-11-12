package com.example.tanify.ui.splashScreen

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.tanify.R
import com.example.tanify.databinding.ActivitySplashScreenBinding
import com.example.tanify.ui.MainActivity
import com.example.tanify.ui.login.LoginActivity

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding
    private lateinit var sharedPreferences: SharedPreferences
    private val SPLASH_TIMEOUT = 2000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Handler().postDelayed({
            val isLoggedIn = checkLoginStatus()

            if (isLoggedIn) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }

        }, SPLASH_TIMEOUT)
    }

    private fun checkLoginStatus(): Boolean {
        val token = getTokenFromPreferences()
        return !token.isNullOrEmpty()
    }

    private fun getTokenFromPreferences(): String? {
        sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        return sharedPreferences.getString("token", "")
    }
}