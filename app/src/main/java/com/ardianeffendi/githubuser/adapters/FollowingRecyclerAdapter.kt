package com.ardianeffendi.githubuser.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ardianeffendi.githubuser.R
import com.ardianeffendi.githubuser.models.FollowingResponseItem
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_row_user.view.*

class FollowingRecyclerAdapter : RecyclerView.Adapter<FollowingRecyclerAdapter.ListViewHolder>() {

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<FollowingResponseItem>() {
        override fun areItemsTheSame(
            oldItem: FollowingResponseItem,
            newItem: FollowingResponseItem
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: FollowingResponseItem,
            newItem: FollowingResponseItem
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_row_user,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val user = differ.currentList[position]
        holder.itemView.apply {
            Glide.with(this)
                .load(user.avatar_url)
                .apply(RequestOptions().override(55, 55))
                .into(img_item_avatar)
            tv_name.text = user.login
        }
    }
}