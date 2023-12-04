package com.example.tanify.ui.lms.checklistMateri

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.tanify.R
import com.example.tanify.data.api.tanify.ApiConfig
import com.example.tanify.databinding.ActivityCheckListMateriBinding
import com.example.tanify.databinding.ActivityLmsBinding
import com.example.tanify.ui.lms.LmsActivity

class CheckListMateriActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCheckListMateriBinding
    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var recyclerviewSection: RecyclerView
    companion object {
        private const val TAG = "CheckListMateri"
        private var TOKEN = "token"
        private var IDModul = 0

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckListMateriBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // sharedPreferences data
        sharedPreferences = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        TOKEN = sharedPreferences.getString("token", "").toString()

        // set recyclerview
        recyclerviewSection = findViewById(R.id.rv_checklist_section)

        // get id
        IDModul = intent.getIntExtra("idModul", 0)
        Log.d(TAG, "id Modul : "+ IDModul)

        getdata()
        startAction()

    }

    private fun startAction() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun getdata() {
        val tag = TAG+" : get data form API"
        ApiConfig.instanceRetrofit

    }
}