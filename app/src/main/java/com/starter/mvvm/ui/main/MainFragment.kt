package com.starter.mvvm.ui.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.starter.mvvm.data.remote.BaseResponse
import com.starter.mvvm.data.remote.entites.User
import com.starter.mvvm.databinding.FragmentMainBinding
import com.starter.mvvm.ui.details.container.DetailsActivity
import com.starter.mvvm.ui.main.adapter.UserListAdapter
import com.starter.mvvm.utils.PagedListFooterType
import com.starter.mvvm.utils.RetryListener
import com.starter.mvvm.utils.Status
import com.starter.mvvm.utils.base.BaseFragment
import com.starter.mvvm.utils.extensions.observe
import com.starter.mvvm.utils.extensions.user
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding>(FragmentMainBinding::inflate) {

    private val viewModel by viewModels<MainViewModel>()
    private var adapter: UserListAdapter? = null
    private var activityResultLauncher: ActivityResultLauncher<Intent>? = null

    override fun getCurrentViewModel() = viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    // There are no request codes
                    val data: Intent? = result.data
                    data?.extras?.user?.let {
                        showMsg(it.email)
                    }
                }
            }
        super.onCreate(savedInstanceState)
    }

    override fun onViewReady() {
        observe(viewModel.status) {
            when (it) {
                is Status.Loading -> {
                    binding.btnRetry.visibility = View.GONE
                    // show shimmer or custom loading
                    showDialogLoading()
                }
                is Status.LoadingMore -> handleLoadingMore()
                is Status.Success<*> -> handleSuccess((it.data as BaseResponse<List<User>>).data)
                is Status.SuccessLoadingMore -> handleSuccessLoadingMore()
                is Status.Error<*> -> onError(it) {
                    binding.btnRetry.visibility = View.VISIBLE
                    hideDialogLoading()
                }
                is Status.ErrorLoadingMore<*> -> onError(it) { handleErrorLoadingMore() }
            }
        }
        initViews()
        initPagedList()
    }

    private fun initViews() {
        binding.srLayout.setOnRefreshListener {
            binding.srLayout.isRefreshing = false
            viewModel.refresh()
        }
        binding.btnRetry.setOnClickListener {
            viewModel.retry()
        }
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
                viewModel.retry()
            }
        }) {
            DetailsActivity.start(
                this, it?.id ?: -1, requireNotNull(activityResultLauncher)
            )
        }
        binding.rvUsers.adapter = adapter
        binding.rvUsers.itemAnimator = null
    }

    private fun handleLoadingMore() {
        adapter?.setFooter(PagedListFooterType.Loading)
    }

    private fun handleSuccessLoadingMore() {
        adapter?.setFooter(PagedListFooterType.None)
    }

    private fun handleErrorLoadingMore() {
        adapter?.setFooter(PagedListFooterType.Retry)
    }

    private fun handleSuccess(list: List<User>?) {
        hideDialogLoading()
        // handle if no data
    }
}
