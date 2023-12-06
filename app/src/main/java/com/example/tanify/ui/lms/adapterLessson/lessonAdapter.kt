package com.example.tanify.ui.lms.adapterLessson

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tanify.R
import com.example.tanify.data.response.lms.ModulItem
import com.example.tanify.ui.lms.checklistMateri.CheckListMateriActivity

class lessonAdapter(private val dataset: List<ModulItem>, private val tipe :Int) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_ITEM_1 = 1
        private const val TYPE_ITEM_2 = 2
        private const val TYPE_ITEM_3 = 3
    }

    inner class ItemViewHolder1(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val title: TextView = itemView.findViewById(R.id.itemRekomendasiTitle)
        val section: TextView = itemView.findViewById(R.id.itemRekomendasiSection)
        val cover: ImageView = itemView.findViewById(R.id.itemRekomendasiCover)
        val item:ConstraintLayout = itemview.findViewById(R.id.item)
    }

    inner class ItemViewHolder2(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val title:TextView = itemView.findViewById(R.id.tvTitleyourlessons)
        val section :TextView = itemView.findViewById(R.id.tvSection)
        val cover :ImageView = itemView.findViewById(R.id.ivYourLessons)
        val prosesbar:LinearLayout = itemview.findViewById(R.id.prosesbarmodul)
        val lineprosesbar : ProgressBar = itemview.findViewById(R.id.simpleProgressBar)
        val textprosesbar :TextView = itemView.findViewById(R.id.textprosesbar)
        val item: LinearLayout= itemview.findViewById(R.id.item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_ITEM_1 -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_rekomendasi_lms, parent, false)
                ItemViewHolder1(view)
            }

            TYPE_ITEM_2 , TYPE_ITEM_3-> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_your_lessons, parent, false)
                ItemViewHolder2(view)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            TYPE_ITEM_1 -> {
                val viewHolder = holder as ItemViewHolder1
                viewHolder.title.text = dataset[position].title
                viewHolder.section.text = dataset[position].section.toString() + " Section"
                val foto = dataset[position].cover
                Glide.with(viewHolder.itemView.context)
                    .load("http://195.35.32.179:8001/$foto")
                    .skipMemoryCache(false)
                    .placeholder(R.drawable.gambar_checklist)
                    .error(R.drawable.icon_user)
                    .into(viewHolder.cover)

                viewHolder.item.setOnClickListener{
                    val context = viewHolder.itemView.context
                    startActivityModul(context, dataset[position].id)
                }
            }

            TYPE_ITEM_2 -> {
                val viewHolder = holder as ItemViewHolder2
                viewHolder.prosesbar.visibility = View.GONE
                viewHolder.title.text = dataset[position].title
                viewHolder.section.text = dataset[position].section.toString() + " Section"
                val foto = dataset[position].cover
                Glide.with(viewHolder.itemView.context)
                    .load("http://195.35.32.179:8001/$foto")
                    .skipMemoryCache(false)
                    .placeholder(R.drawable.gambar_checklist)
                    .error(R.drawable.icon_user)
                    .into(viewHolder.cover)

                viewHolder.item.setOnClickListener{
                    val context = viewHolder.itemView.context
                    startActivityModul(context, dataset[position].id)
                }
            }

            TYPE_ITEM_3 -> {
                val viewHolder = holder as ItemViewHolder2
                viewHolder.title.text = dataset[position].title
                viewHolder.section.text = dataset[position].totalsection.toString() + " Section"
                viewHolder.textprosesbar.text = dataset[position].progres.toString()+"% Complated"
                viewHolder.lineprosesbar.progress = dataset[position].progres!!

                val foto = dataset[position].cover
                Glide.with(viewHolder.itemView.context)
                    .load("http://195.35.32.179:8001/$foto")
                    .skipMemoryCache(false)
                    .placeholder(R.drawable.gambar_checklist)
                    .error(R.drawable.icon_user)
                    .into(viewHolder.cover)

                viewHolder.item.setOnClickListener{
                    val context = viewHolder.itemView.context
                    startActivityModul(context, dataset[position].id)
                }
            }

        }
    }

    private fun startActivityModul(context: Context, id: Int?) {
        val intent = Intent(context, CheckListMateriActivity::class.java)
        intent.putExtra("idModul", id)
        context.startActivity(intent)
    }


    override fun getItemCount() = dataset.size

    override fun getItemViewType(position: Int): Int {
        // Determine the type of item based on position or data
        return when (tipe) {
            TYPE_ITEM_1 -> TYPE_ITEM_1
            TYPE_ITEM_2 -> TYPE_ITEM_2
            TYPE_ITEM_3 -> TYPE_ITEM_3
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }
}
