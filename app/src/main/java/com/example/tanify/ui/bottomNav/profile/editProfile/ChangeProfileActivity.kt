package com.example.tanify.ui.bottomNav.profile.editProfile

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.tanify.R
import com.example.tanify.databinding.ActivityChangePasswordBinding
import com.example.tanify.databinding.ActivityChangeProfileBinding
import com.example.tanify.databinding.ActivityMainBinding
import com.example.tanify.databinding.FragmentProfileBinding

class ChangeProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChangeProfileBinding
    private lateinit var sharedPreferences: SharedPreferences
    companion object {
        private const val TAG = "ChangePassword"
        private const val PREF_NAME = "MyAppPreferences"
        private var TOKEN = "token"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangeProfileBinding.inflate(layoutInflater)
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
    }
}