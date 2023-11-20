package com.example.tanify.ui.bottomNav.beranda.items

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tanify.R
import com.example.tanify.data.data.ArtikelBerandaItemData
import com.example.tanify.ui.artikel.DetailArtikelActivity

class ItemBerandaArtikelAdapter(private val artikelList: List<ArtikelBerandaItemData>):
    RecyclerView.Adapter<ItemBerandaArtikelAdapter.ItemBerandaArtikelAdapterViewHolder>() {
    class ItemBerandaArtikelAdapterViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val poster: ImageView = itemView.findViewById(R.id.iv_poster_artikel_beranda)
        val title: TextView = itemView.findViewById(R.id.tv_title_poster_beranda)

        fun bindView(){
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailArtikelActivity::class.java)
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemBerandaArtikelAdapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_artikel_beranda, parent, false)
        return ItemBerandaArtikelAdapterViewHolder(view)
    }

    override fun getItemCount(): Int = artikelList.size

    override fun onBindViewHolder(holder: ItemBerandaArtikelAdapterViewHolder, position: Int) {
        holder.poster.setImageResource(artikelList[position].poster)
        holder.title.text = artikelList[position].title
        holder.bindView()
    }
}