package com.starter.mvvm.utils.base

import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.starter.mvvm.databinding.ItemPagedListFooterBinding
import com.starter.mvvm.utils.PagedListFooterType
import com.starter.mvvm.utils.RetryListener

/**
 * Created by Norhan Elsawi on 23/01/2020.
 */
abstract class BasePagedListAdapter<T>(
    DIFF_CALLBACK: DiffUtil.ItemCallback<T>,
) : PagedListAdapter<T, RecyclerView.ViewHolder>(
    DIFF_CALLBACK
) {

    var pagedListFooter: PagedListFooterType = PagedListFooterType.None

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasFooter()) 1 else 0
    }

    fun getActualListItemCount(): Int {
        return super.getItemCount()
    }

    fun setFooter(pagedListFooter: PagedListFooterType) {
        if (this.pagedListFooter != pagedListFooter) {
            this.pagedListFooter = pagedListFooter
            notifyDataSetChanged()
        }
    }

    private fun hasFooter(): Boolean {
        return super.getItemCount() != 0 && (pagedListFooter is PagedListFooterType.Loading || pagedListFooter is PagedListFooterType.Retry)
    }

    inner class FooterViewHolder(
        binding: ItemPagedListFooterBinding,
        retryListener: RetryListener,
    ) :
        BaseListFooterViewHolder(binding, retryListener) {

        override fun getPagedListFooterType(): PagedListFooterType {
            return pagedListFooter
        }
    }
}
