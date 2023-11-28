package com.example.tanify.ui.artikel

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tanify.data.api.tanify.ApiConfig
import com.example.tanify.data.response.Artikel
import com.example.tanify.data.response.ArtikelResponse
import com.example.tanify.databinding.ActivityArtikelBinding
import com.example.tanify.ui.artikel.items.ItemArtikelAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ArtikelActivity : AppCompatActivity(), ItemArtikelAdapter.OnArtikelItemClickListener {

    private lateinit var binding: ActivityArtikelBinding
    private lateinit var artikelAdapter: ItemArtikelAdapter

    companion object {
        private const val TAG = "ArtikelActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArtikelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setRecyclerView()
        getArtikelData()
        setAction()
    }

    private fun setRecyclerView(){
        artikelAdapter = ItemArtikelAdapter(this, emptyList())
        artikelAdapter.setOnArtikelItemClickListener(this)
        binding.rvArtikelActivity.layoutManager = LinearLayoutManager(this)
        binding.rvArtikelActivity.adapter = artikelAdapter
    }

    private fun setAction(){
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun getArtikelData() {
        showLoading(true)
        ApiConfig.instanceRetrofit.getArtikel()
            .enqueue(object : Callback<ArtikelResponse>{
                override fun onResponse(
                    call: Call<ArtikelResponse>,
                    response: Response<ArtikelResponse>
                ) {
                    showLoading(false)
                    if (response.isSuccessful) {
                        val artikelResponse = response.body()

                        // Update RecyclerView with the received data
                        if (artikelResponse?.data != null) {
                            artikelAdapter.updateData(artikelResponse.data)
                        } else {
                            // Handle jika data dari API kosong atau tidak valid
                            Toast.makeText(this@ArtikelActivity, "Data tidak ditemukan", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        // Handle jika respon tidak berhasil (misalnya kode respon bukan 200 OK)
                        Toast.makeText(this@ArtikelActivity, "Gagal mendapatkan data", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ArtikelResponse>, t: Throwable) {
                    Toast.makeText(this@ArtikelActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    showLoading(false)
                }
            })
    }

    private fun showLoading(isLoading: Boolean){
        if (isLoading) {
            binding.progressCircular.visibility = View.VISIBLE
        } else {
            binding.progressCircular.visibility = View.GONE
        }
    }

    override fun onArtikelItemClicked(artikel: Artikel) {
        val id = artikel.id
        Log.d(TAG, "id artikel yang dipilih = $id")
        val intent = Intent(this, DetailArtikelActivity::class.java)
        intent.putExtra("ARTIKEL_ID", artikel.id)
        startActivity(intent)
    }
}