package com.starter.mvvm.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.asala.asala.utils.RetryListener
import com.starter.mvvm.data.remote.BaseResponse
import com.starter.mvvm.data.remote.entites.User
import com.starter.mvvm.databinding.FragmentMainBinding
import com.starter.mvvm.ui.main.adapter.UserListAdapter
import com.starter.mvvm.utils.PagedListFooterType
import com.starter.mvvm.utils.Status
import com.starter.mvvm.utils.base.BaseFragment
import com.starter.mvvm.utils.extensions.observe
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding>() {

    private val viewModel by viewModels<MainViewModel>()
    private var adapter: UserListAdapter? = null

    override fun initViewModel() = viewModel

    override fun setupViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentMainBinding = FragmentMainBinding.inflate(inflater, container, false)

    override fun onViewReady() {
        observe(viewModel.status) {
            when (it) {
                is Status.Loading -> showDialogLoading()
                is Status.LoadingMore -> handleLoadingMore()
                is Status.Success<*> -> handleSuccess((it.data as BaseResponse<List<User>>).data)
                is Status.SuccessLoadingMore -> handleSuccessLoadingMore()
                is Status.Error<*> -> onError(it) {
                    hideDialogLoading()
                    showErrorMsg(it.errorResponse.message)
                }
                is Status.ErrorLoadingMore<*> -> onError(it) { handleErrorLoadingMore() }
            }
        }
        initPagedList()
    }

    private fun initPagedList() {
        initRecyclerView()
        viewModel.initPagedList()
        observe(viewModel.pagedList) {
            adapter?.submitList(it)
        }
    }

    private fun initRecyclerView() {
        adapter = UserListAdapter(object : RetryListener {
            override fun onRetry() {
                // viewModel.retry()
            }
        }) {
        }
        binding.rvUsers.adapter = adapter
        binding.rvUsers.itemAnimator = null
    }

    private fun handleLoadingMore() {
        setFooter(PagedListFooterType.Loading)
    }

    private fun handleSuccessLoadingMore() {
        setFooter(PagedListFooterType.None)
    }

    private fun handleErrorLoadingMore() {
        setFooter(PagedListFooterType.Retry)
    }

    private fun setFooter(pagedListFooter: PagedListFooterType) {
        adapter?.setFooter(pagedListFooter)
    }

    private fun handleSuccess(list: List<User>?) {
        hideDialogLoading()
    }

}