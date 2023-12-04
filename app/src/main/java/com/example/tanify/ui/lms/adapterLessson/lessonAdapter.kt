package com.example.tanify.ui.lms.adapterLessson

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tanify.R
import com.example.tanify.data.response.lms.ModulItem

class rekomendasiAdapter(private val dataset: List<ModulItem>)
    :RecyclerView.Adapter<rekomendasiAdapter.itemViewHolder>() {
    inner class itemViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview){
        val title :TextView = itemView.findViewById(R.id.itemRekomendasiTitle)
        val section :TextView = itemView.findViewById(R.id.itemRekomendasiSection)
        val cover:ImageView = itemView.findViewById(R.id.itemRekomendasiCover)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): itemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_rekomendasi_lms, parent, false)
        return itemViewHolder(view)
    }

    override fun onBindViewHolder(holder: rekomendasiAdapter.itemViewHolder, position: Int) {
        holder.title.text = dataset[position].title
        holder.section.text = dataset[position].section.toString()+" Section"
        val foto = dataset[position].cover
        Glide.with(holder.itemView.context)
            .load("http://195.35.32.179:8001/" + foto)
            .skipMemoryCache(false)
            .placeholder(R.drawable.icon_user)
            .error(R.drawable.icon_user)
            .into(holder.cover)
    }


    override fun getItemCount() = dataset.size
}