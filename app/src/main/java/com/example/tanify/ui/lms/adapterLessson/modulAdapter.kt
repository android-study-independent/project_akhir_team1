package com.example.tanify.ui.lms.adapterLessson

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.tanify.R
import com.example.tanify.data.response.lms.SectionItem
import com.example.tanify.ui.lms.checklistMateri.CheckListMateriActivity
import com.example.tanify.ui.lms.detailMateri.DetailMateriActivity

class modulAdapter(private val dataset:List<SectionItem>, private val idModul :Int)
    : RecyclerView.Adapter<modulAdapter.viewHiolder>() {
    inner class viewHiolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val title:TextView = itemView.findViewById(R.id.title_section)
        val progres:ImageView = itemView.findViewById(R.id.progres_section)
        val item:CardView = itemView.findViewById(R.id.item)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHiolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_check_list_materi, parent, false)
        return viewHiolder(view)
    }

    override fun getItemCount() = dataset.size

    override fun onBindViewHolder(holder: viewHiolder, position: Int) {
        holder.title.text = dataset[position].section
        // Set image based on the progres status
        if (dataset[position].progres == null) {
            holder.progres.setImageResource(R.drawable.icon_check_false)
        } else {
            holder.progres.setImageResource(R.drawable.icon_check_true)
        }

        holder.item.setOnClickListener{
            val context = holder.itemView.context
            val intent = Intent(context, DetailMateriActivity::class.java)
            val bundle = Bundle()
            bundle.putInt("idSection", dataset[position].id_section?.toInt() ?: 0)
            bundle.putInt("idModul", idModul)

            intent.putExtras(bundle)
            Log.d("kirim data : ", dataset[position].id_section+" : "+idModul)
            context.startActivity(intent)
        }
    }
}