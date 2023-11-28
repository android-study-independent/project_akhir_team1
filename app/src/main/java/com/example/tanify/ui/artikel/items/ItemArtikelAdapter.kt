package com.example.tanify.ui.artikel.items

import android.annotation.SuppressLint
import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tanify.data.response.Artikel
import com.example.tanify.databinding.ItemListArtikelActivityBinding
import com.example.tanify.helper.formatDate
import com.example.tanify.helper.getFirstName

class ItemArtikelAdapter(
    private val context: Context,
    private var artikelList: List<Artikel>
) : RecyclerView.Adapter<ItemArtikelAdapter.ArtikelViewHolder>() {

    interface OnArtikelItemClickListener{
        fun onArtikelItemClicked(artikel: Artikel)
    }

    private var onArtikelItemClickListener: OnArtikelItemClickListener? = null

    fun setOnArtikelItemClickListener(listener: OnArtikelItemClickListener){
        onArtikelItemClickListener = listener
    }

    inner class ArtikelViewHolder(internal val binding: ItemListArtikelActivityBinding) :
        RecyclerView.ViewHolder(binding.root) {
            init {
                binding.root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val clickedArtikel = artikelList[position]
                        onArtikelItemClickListener?.onArtikelItemClicked(clickedArtikel)
                    }
                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtikelViewHolder {
        val binding = ItemListArtikelActivityBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ArtikelViewHolder(binding)
    }

    override fun getItemCount(): Int = artikelList.size

    override fun onBindViewHolder(holder: ArtikelViewHolder, position: Int) {
        val currentArtikel = artikelList[position]
        val deskripsi = Html.fromHtml(currentArtikel.deskripsi)

        holder.binding.tvNamapembuatArtikel.text = getFirstName(currentArtikel.pembuat!!)
        holder.binding.tvTanggalArtikel.text = formatDate(currentArtikel.createdAt!!)
        holder.binding.tvJudulArtikel.text = currentArtikel.title
        holder.binding.deskripsiListArtikel.text = deskripsi

        Glide.with(context)
            .load("http://195.35.32.179:8001${currentArtikel.cover}")
            .into(holder.binding.ivListartikel)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newArticleList: List<Artikel?>?){
        artikelList = newArticleList?.filterNotNull() ?: emptyList()
        notifyDataSetChanged()
    }
}
