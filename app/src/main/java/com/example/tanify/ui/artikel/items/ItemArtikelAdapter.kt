package com.example.tanify.ui.artikel.items

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tanify.data.response.Artikel
import com.example.tanify.databinding.ItemListArtikelActivityBinding

class ItemArtikelAdapter(
    private val context: Context,
    private var artikelList: List<Artikel>
) : RecyclerView.Adapter<ItemArtikelAdapter.ArtikelViewHolder>() {
    inner class ArtikelViewHolder(internal val binding: ItemListArtikelActivityBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtikelViewHolder {
        val binding = ItemListArtikelActivityBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ArtikelViewHolder(binding)
    }

    override fun getItemCount(): Int = artikelList.size

    override fun onBindViewHolder(holder: ArtikelViewHolder, position: Int) {
        val currentArtikel = artikelList[position]

        holder.binding.tvNamapembuatArtikel.text = currentArtikel.pembuat
        holder.binding.tvTanggalArtikel.text = currentArtikel.createdAt
        holder.binding.tvJudulArtikel.text = currentArtikel.title
        holder.binding.deskripsiListArtikel.text = currentArtikel.deskripsi

        Glide.with(context)
            .load("http://195.35.32.179:8001${currentArtikel.cover}")
            .into(holder.binding.ivListartikel)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newArticleList: List<Artikel?>?){
        artikelList = newArticleList as List<Artikel>
        notifyDataSetChanged()
    }
}
