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
    private var onClickLike: () -> Unit,
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
        val baseUrl = "http://195.35.32.179:8001"
        val tanggal = formatDate(currentForum.createdAt!!)
        val posterPathFix = currentForum.cover?.removePrefix("..")
        val poster = baseUrl + posterPathFix
        val profilePathFix = currentForum.createdBy?.photo?.removePrefix("..")
        val profile = baseUrl + profilePathFix

        holder.binding.tvTanggalForum.text = tanggal
        holder.binding.tvJudulForum.text = currentForum.title
        holder.binding.tvIsiKontenForum.text = currentForum.content
        holder.binding.tvNameCreatorForum.text = currentForum.createdBy?.nama

        Glide.with(context)
            .load(profile)
            .placeholder(R.drawable.bg_load_profile)
            .into(holder.binding.ivProfileForum)

        Glide.with(context)
            .load(poster)
            .placeholder(R.drawable.bg_load_poster_forum)
            .into(holder.binding.ivItemForum)

        holder.binding.heartForumButton.setOnClickListener {
            onClickLike()
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
