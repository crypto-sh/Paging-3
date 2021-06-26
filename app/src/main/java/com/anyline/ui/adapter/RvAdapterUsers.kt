package com.anyline.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.anyline.databinding.LayoutItemUserBinding
import com.anyline.dto.User
import timber.log.Timber


class RvAdapterUsers(private val onUserSelected : (User) -> Unit) : PagingDataAdapter<User, RvAdapterUsers.Holder>(object : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean = oldItem == newItem
        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean =
            oldItem.id == newItem.id
    }) {

    inner class Holder(private val binding: LayoutItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User?) {
            if (user != null) {
                binding.user = user
                binding.constraint.setOnDelayClickListener {
                    onUserSelected(user)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            LayoutItemUserBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

}