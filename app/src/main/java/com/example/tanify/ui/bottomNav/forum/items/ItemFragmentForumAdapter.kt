package com.example.tanify.ui.bottomNav.forum.items

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tanify.data.response.forum.DataItem
import com.example.tanify.databinding.ItemListFragmentForumBinding
import com.example.tanify.helper.formatDate
import com.example.tanify.R

class ItemFragmentForumAdapter(
    private val context: Context,
    private var listForum: List<DataItem>,
    private var onClick: (forum: DataItem) -> Unit,
    private var onClickLike: () -> Unit
) : RecyclerView.Adapter<ItemFragmentForumAdapter.ItemFragmentForumHolder>(){

//    interface OnItemFragmentForumClickListener{
//        fun onItemFragmentForumClickListener(forum: DataItem)
//    }
//
//    private var onItemFragmentForumClickListener: OnItemFragmentForumClickListener? = null
//
//    fun setonItemFragmentForumClickListener(listener: OnItemFragmentForumClickListener)

    inner class ItemFragmentForumHolder(internal val binding: ItemListFragmentForumBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemFragmentForumHolder {
        val binding = ItemListFragmentForumBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ItemFragmentForumHolder(binding)
    }

    override fun getItemCount(): Int = listForum.size

    override fun onBindViewHolder(holder: ItemFragmentForumHolder, position: Int) {
        val currentForum = listForum[position]
        val tanggal = formatDate(currentForum.createdAt!!)
        val poster = "http://195.35.32.179:8001${currentForum.cover}"

        holder.binding.tvTanggalForum.text = tanggal
        holder.binding.tvJudulForum.text = currentForum.title
        holder.binding.tvIsiKontenForum.text = currentForum.content

        Glide.with(context)
            .load(poster)
            .placeholder(R.drawable.bg_view_holder_image_item_forum)
            .into(holder.binding.ivItemForum)

        holder.binding.heartForumButton.setOnClickListener {
            onClickLike()
        }

        holder.binding.containerForum.setOnClickListener {
            onClick(currentForum)
        }
    }
}
