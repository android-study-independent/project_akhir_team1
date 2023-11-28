package com.example.tanify.ui.artikel

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.tanify.R
import com.example.tanify.data.api.tanify.ApiConfig
import com.example.tanify.data.response.ArtikelResponse
import com.example.tanify.databinding.ActivityDetailArtikelBinding
import com.example.tanify.helper.formatDate
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Suppress("DEPRECATION")
class DetailArtikelActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailArtikelBinding
    private  var id: Int = 0

    companion object {
        private const val TAG = "DetailArtikelActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailArtikelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        id = intent.getIntExtra("ARTIKEL_ID", 0)
        Log.d(TAG, "Artikel id : $id")

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
        //set artikel konten
        getArtikelData()
    }

    private fun setAction(){
        binding.btnBackToolbar.setOnClickListener {
            Log.d(TAG, "btn toolbar")
            onBackPressed()
        }
        binding.btnBackCollapse.setOnClickListener {
            Log.d(TAG, "btn colapse")
            onBackPressed()
        }
        binding.btnFontSize.setOnClickListener {
            changeTextSize()
        }
    }

    private fun setArtikelData(cover: String, judul: String, pembuat: String, tanggal: String, konten: Spanned){
        binding.tvTitleColapse.text = judul
        binding.tvTitleArtikel.text = judul
        binding.tvAuthorName.text = "Oleh $pembuat"
        binding.tvDateDetailArtikel.text = tanggal
        binding.tvDescription.text = konten
        Glide.with(this)
            .load(cover)
            .into(binding.imageViewPoster)
    }

    private fun getArtikelData() {
        ApiConfig.instanceRetrofit.getArtikel()
            .enqueue(object : Callback<ArtikelResponse> {
                override fun onResponse(
                    call: Call<ArtikelResponse>,
                    response: Response<ArtikelResponse>
                ) {
                    if (response.isSuccessful) {
                        val artikelResponse = response.body()

                        if (artikelResponse?.data != null) {
                            val currentArtikel = artikelResponse.data[id-1]
                            val cover = "http://195.35.32.179:8001${currentArtikel?.cover}"
                            val judul = currentArtikel?.title
                            val pembuat = currentArtikel?.pembuat
                            val tanggal = formatDate(currentArtikel?.createdAt.toString())
                            val konten = Html.fromHtml(currentArtikel?.isi)
                            setArtikelData(cover, judul!!, pembuat!!, tanggal, konten)
                        } else {
                            // Handle jika data dari API kosong atau tidak valid
                            Toast.makeText(this@DetailArtikelActivity, "Data tidak ditemukan", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        // Handle jika respon tidak berhasil (misalnya kode respon bukan 200 OK)
                        Toast.makeText(this@DetailArtikelActivity, "Gagal mendapatkan data", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ArtikelResponse>, t: Throwable) {
                    Toast.makeText(this@DetailArtikelActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when(item.itemId) {
//            android.R.id.home -> {
//                onBackPressed()
//                return true
//            }
//            else -> return super.onOptionsItemSelected(item)
//        }
//    }

    private fun changeTextSize(){
        val desc = binding.tvDescription
        val currentSize = desc.textSize

        val spSize = currentSize/resources.displayMetrics.scaledDensity
        if (spSize == 12f) {
            desc.textSize = 14f
        } else {
            desc.textSize = 12f
        }
    }
}