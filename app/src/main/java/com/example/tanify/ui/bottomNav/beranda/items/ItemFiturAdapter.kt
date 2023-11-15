package com.example.tanify.ui.bottomNav.beranda.items

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tanify.data.data.FiturItemData
import com.example.tanify.R

class ItemFiturAdapter(private val fiturList: List<FiturItemData>):
    RecyclerView.Adapter<ItemFiturAdapter.ItemFiturAdapterViewHolder>() {
    inner class ItemFiturAdapterViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val illustrasi: ImageView = itemView.findViewById(R.id.iv_fitur)
        val fitur_title: TextView = itemView.findViewById(R.id.tv_poster_title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemFiturAdapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_fitur_utama, parent, false)
        return ItemFiturAdapterViewHolder(view)
    }

    override fun getItemCount(): Int = fiturList.size

    override fun onBindViewHolder(holder: ItemFiturAdapterViewHolder, position: Int) {
        holder.illustrasi.setImageResource(fiturList[position].ilustrasi)
        holder.fitur_title.text = fiturList[position].title
    }
}