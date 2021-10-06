package com.starter.mvvm.utils.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.asala.asala.utils.RetryListener
import com.starter.mvvm.databinding.ItemPagedListFooterBinding
import com.starter.mvvm.utils.PagedListFooterType

/**
 * Created by Norhan Elsawi on 23/01/2020.
 */
abstract class BaseListFooterViewHolder(
    private val binding: ItemPagedListFooterBinding,
    private val retryListener: RetryListener,
) :
    RecyclerView.ViewHolder(binding.root) {

    abstract fun getPagedListFooterType(): PagedListFooterType

    fun setFooterView() {
        with(binding) {
            if (getPagedListFooterType() is PagedListFooterType.Retry) {
                lTryAgain.llRetry.visibility = View.VISIBLE
                pbLoading.visibility = View.GONE
            } else {
                lTryAgain.llRetry.visibility = View.GONE
                pbLoading.visibility = View.VISIBLE
            }
            lTryAgain.llRetry.setOnClickListener {
                retryListener.onRetry()
            }

        }
    }
}