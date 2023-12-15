package com.example.tanify.ui.bottomNav.forum.items

import android.annotation.SuppressLint
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
    private var onClickLike: (forum: DataItem) -> Unit,
    private var onClickComment: (forum: DataItem) -> Unit
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

        holder.binding.tvTanggalForum.text = tanggal
        holder.binding.tvJudulForum.text = currentForum.title
        holder.binding.tvIsiKontenForum.text = currentForum.content
        holder.binding.tvNameCreatorForum.text = currentForum.createdBy?.nama?.replace("\"","")
        if (currentForum.likes == true) {
            holder.binding.icHeart.setImageResource(R.drawable.ic_heart_fill)
        } else {
            holder.binding.icHeart.setImageResource(R.drawable.ic_like_empty)
        }

        Glide.with(context)
            .load(currentForum.createdBy?.photo)
            .placeholder(R.drawable.ic_profile_blank)
            .into(holder.binding.ivProfileForum)

        Glide.with(context)
            .load(currentForum.cover)
            .placeholder(R.drawable.bg_load_poster_forum)
            .into(holder.binding.ivItemForum)

        holder.binding.btnLike.setOnClickListener {
            onClickLike(currentForum)
        }

        holder.binding.containerForum.setOnClickListener {
            onClick(currentForum)
        }

        holder.binding.btnComment.setOnClickListener {
            onClickComment(currentForum)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateDataForumBeranda(newForumList: List<DataItem>?){
        listForum = newForumList?.filterNotNull() ?: emptyList()
        notifyDataSetChanged()
    }
}
