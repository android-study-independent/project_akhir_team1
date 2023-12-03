package com.example.tanify.ui.lms

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tanify.R
import com.example.tanify.data.response.lms.lessonAllResponse
import com.example.tanify.data.response.profile.UserProfilResponse
import com.example.tanify.databinding.ActivityArtikelBinding
import com.example.tanify.databinding.ActivityLmsBinding
import com.example.tanify.ui.bottomNav.profile.ProfileFragment

class LmsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLmsBinding
    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var userProfil:UserProfilResponse
    private lateinit var ModulLesson:lessonAllResponse


    companion object {
        private const val TAG = "lmsHome"
        private var TOKEN = "token"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLmsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // sharedPreferences data
        sharedPreferences = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        TOKEN = sharedPreferences.getString("token", "").toString()


        startAction()
        setProfil()
        getDataAllModul()
        setRekomendasiModul()
        setListModul()
    }

    private fun setListModul() {
        // ambil data listmodul, dan update ke rvYourlessons
    }

    private fun setRekomendasiModul() {
        // ambil data rekomendarumodul, dan update ke rvListRekomendasi
    }

    private fun getDataAllModul() {
        // ambil data modul dari api
    }

    private fun setProfil() {
        // ambil data profil dari SharedPreferences/token
    }

    private fun startAction() {
        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnsearchLesson.setOnClickListener {
            val textSearch = binding.editTextSearch.text.toString()
            //intent laman baru search
        }
    }

}