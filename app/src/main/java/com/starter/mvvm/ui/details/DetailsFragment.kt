package com.starter.mvvm.ui.details

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.viewModels
import com.starter.mvvm.data.local.entities.TwoUsers
import com.starter.mvvm.data.remote.BaseResponse
import com.starter.mvvm.databinding.FragmentDetailsBinding
import com.starter.mvvm.utils.Status
import com.starter.mvvm.utils.base.BaseFragment
import com.starter.mvvm.utils.extensions.id
import com.starter.mvvm.utils.extensions.observe
import com.starter.mvvm.utils.extensions.user
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : BaseFragment<FragmentDetailsBinding>(FragmentDetailsBinding::inflate) {

    private val viewModel by viewModels<DetailsViewModel>()
    private var twoUsers: TwoUsers? = null

    override fun getCurrentViewModel() = viewModel

    override fun onViewReady() {
        initArguments()
        observe(viewModel.status) {
            when (it) {
                is Status.Loading -> {
                    // show shimmer or custom loading
                    showDialogLoading()
                }
                is Status.Success<BaseResponse<TwoUsers>> -> handleSuccess(it.data?.data)
                is Status.Error<Any> -> onError(it) {
                    hideDialogLoading()
                }
            }
        }
    }

    private fun initArguments() {
        arguments?.id?.let {
            viewModel.getTwoUsers(it)
        }
    }

    private fun handleSuccess(twoUsers: TwoUsers?) {
        hideDialogLoading()
        this.twoUsers = twoUsers
        binding.tvDetails.text = twoUsers.toString()
    }

    fun onBackPressed() {
        Intent().also { intent ->
            Bundle().also { bundle ->
                bundle.user = twoUsers?.user2
                intent.putExtras(bundle)
            }
            activity?.setResult(Activity.RESULT_OK, intent)
            activity?.finish()
        }
    }

    companion object {
        fun getInstance(bundle: Bundle?) = DetailsFragment().also {
            it.arguments = bundle
        }
    }
}
