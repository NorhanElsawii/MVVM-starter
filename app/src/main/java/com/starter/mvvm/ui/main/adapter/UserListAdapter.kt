package com.starter.mvvm.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.starter.mvvm.data.remote.entites.User
import com.starter.mvvm.databinding.ItemPagedListFooterBinding
import com.starter.mvvm.databinding.ItemUserBinding
import com.starter.mvvm.utils.RetryListener
import com.starter.mvvm.utils.base.BasePagedListAdapter

class UserListAdapter(
    private val retryListener: RetryListener,
    private val onItemClicked: (user: User?) -> Unit,
) :
    BasePagedListAdapter<User>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE -> ItemViewHolder(
                ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
            else -> FooterViewHolder(
                ItemPagedListFooterBinding.inflate(LayoutInflater.from(parent.context),
                    parent,
                    false), retryListener
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when {
            getItemViewType(position) == FOOTER_VIEW_TYPE -> (holder as BasePagedListAdapter<*>.FooterViewHolder).setFooterView()
            else -> (holder as ItemViewHolder).bind(getItem(position))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < super.getActualListItemCount() && getItem(position) != null)
            ITEM_VIEW_TYPE
        else
            FOOTER_VIEW_TYPE
    }


    companion object {
        var DIFF_CALLBACK: DiffUtil.ItemCallback<User> =
            object : DiffUtil.ItemCallback<User>() {
                override fun areItemsTheSame(
                    oldItem: User,
                    newItem: User,
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: User,
                    newItem: User,
                ): Boolean {
                    return oldItem == newItem
                }
            }

        const val FOOTER_VIEW_TYPE = 1
        const val ITEM_VIEW_TYPE = 2
    }

    inner class ItemViewHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: User?) {
            with(binding) {
                tvName.text = item?.first_name
                this.root.setOnClickListener {
                    onItemClicked.invoke(item)
                }
            }
        }
    }
}