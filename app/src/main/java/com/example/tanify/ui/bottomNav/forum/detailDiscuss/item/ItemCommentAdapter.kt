package com.example.tanify.ui.bottomNav.forum.detailDiscuss.item

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tanify.data.response.forum.CommentItem
import com.example.tanify.databinding.ItemCommentBinding
import com.example.tanify.helper.getTimeAgo
import com.example.tanify.R

class ItemCommentAdapter(
    private val context: Context,
    private var listComment: List<CommentItem>,
    private val clickLike: () -> Unit
): RecyclerView.Adapter<ItemCommentAdapter.ItemCommentViewHolder>() {
    inner class ItemCommentViewHolder(internal val binding: ItemCommentBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemCommentViewHolder {
        val binding = ItemCommentBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ItemCommentViewHolder(binding)
    }

    override fun getItemCount(): Int = listComment.size

    override fun onBindViewHolder(holder: ItemCommentViewHolder, position: Int) {
        val currentComment = listComment[position]
        val pathProfile = currentComment.user?.photo
        val profilePathFix = pathProfile?.removePrefix("..")
        val profileImg = "http://195.35.32.179:8001${profilePathFix}"

        holder.binding.tvName.text = currentComment.user?.nama?.replace("\"", "")
        holder.binding.tvWaktu.text = getTimeAgo(currentComment.createdAt.toString())
        holder.binding.tvCommentMessage.text = currentComment.text

        Glide.with(context)
            .load(currentComment.user?.photo)
            .placeholder(R.color.grey)
            .into(holder.binding.ivProfile)

        holder.binding.bntLikeComment.setOnClickListener {
            clickLike()
            Log.d("Poto", profileImg)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateDataComment(newCommentList: List<CommentItem?>?){
        listComment = newCommentList?.filterNotNull() ?: emptyList()
        notifyDataSetChanged()
    }

}