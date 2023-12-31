package com.example.tanify.ui.bottomNav.beranda.items

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tanify.data.response.artikel.Artikel
import com.example.tanify.databinding.ItemArtikelBerandaBinding
import com.example.tanify.helper.formatDate
import com.example.tanify.helper.setWordLimit

class ItemBerandaArtikelAdapter(
    private val context: Context,
    private var artikelList: List<Artikel>
) :
    RecyclerView.Adapter<ItemBerandaArtikelAdapter.ItemBerandaArtikelAdapterViewHolder>() {

    interface OnArtikelBerandaItemClickListener{
        fun onArtikelBerandaItemClickListener(artikel: Artikel)
    }

    private var onArtikelBerandaItemClickListener: OnArtikelBerandaItemClickListener? = null

    fun setOnArtikelBerandaItemClickListener(listener: OnArtikelBerandaItemClickListener){
        onArtikelBerandaItemClickListener = listener
    }
    inner class ItemBerandaArtikelAdapterViewHolder(internal val binding: ItemArtikelBerandaBinding) :
        RecyclerView.ViewHolder(binding.root){
            init {
                binding.root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val clickedArtikel = artikelList[position]
                        onArtikelBerandaItemClickListener?.onArtikelBerandaItemClickListener(clickedArtikel)
                    }
                }
            }
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemBerandaArtikelAdapterViewHolder {
        val binding = ItemArtikelBerandaBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ItemBerandaArtikelAdapterViewHolder(binding)
    }

    override fun getItemCount(): Int = artikelList.size

    override fun onBindViewHolder(holder: ItemBerandaArtikelAdapterViewHolder, position: Int) {
        val currentArtikel = artikelList[position]
        val posterPath = "http://195.35.32.179:8001${currentArtikel.cover}"
        val tanggal = formatDate(currentArtikel.createdAt!!)

        holder.binding.tvTanggalArtikelHomePage.text = tanggal
        setWordLimit(
            holder.binding.tvTitlePosterBeranda,
            currentArtikel.title!!,
            6
        )

        Glide.with(context)
            .load(posterPath)
            .into(holder.binding.ivPosterArtikelBeranda)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateDataArtikelBeranda(newArtikelList: List<Artikel?>?){
        artikelList = newArtikelList?.filterNotNull() ?: emptyList()
        notifyDataSetChanged()
    }
}