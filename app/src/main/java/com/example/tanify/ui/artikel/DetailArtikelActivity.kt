package com.example.tanify.ui.artikel

import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.example.tanify.R
import com.example.tanify.databinding.ActivityDetailArtikelBinding

class DetailArtikelActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailArtikelBinding
    private var isTitleVisible = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailArtikelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setView()
        setAction()
    }

    private fun setView(){
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_left)

        binding.appBarLayout.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            val totalScrollRange = appBarLayout.totalScrollRange

            if (Math.abs(verticalOffset) == totalScrollRange) {
                // Collapsed title
                binding.tvTitleColapse.visibility = View.INVISIBLE
                binding.tvTitleArtikel.visibility = View.VISIBLE
                // Collapsed button
                binding.btnBackCollapse.visibility = View.INVISIBLE
                binding.btnBackToolbar.visibility = View.VISIBLE

                binding.toolbar.background = ColorDrawable(resources.getColor(R.color.white))
            } else {
                binding.tvTitleColapse.visibility = View.VISIBLE
                binding.tvTitleArtikel.visibility = View.INVISIBLE

                binding.btnBackCollapse.visibility = View.VISIBLE
                binding.btnBackToolbar.visibility = View.INVISIBLE

                binding.toolbar.background = ColorDrawable(resources.getColor(R.color.transparant))
            }
        }
    }

    private fun setAction(){
        binding.btnBackToolbar.setOnClickListener {
            onBackPressed()
        }
        binding.btnBackCollapse.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}