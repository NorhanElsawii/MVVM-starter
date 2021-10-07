package com.starter.mvvm.ui.details

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.viewModels
import com.starter.mvvm.data.remote.BaseResponse
import com.starter.mvvm.data.remote.entites.User
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
    private var user: User? = null

    override fun getCurrentViewModel() = viewModel

    override fun onViewReady() {
        initArguments()
        observe(viewModel.status) {
            when (it) {
                is Status.Loading -> {
                    // show shimmer or custom loading
                    showDialogLoading()
                }
                is Status.Success<*> -> handleSuccess((it.data as BaseResponse<User>).data)
                is Status.Error<*> -> onError(it) {
                    hideDialogLoading()
                }
            }
        }
    }

    private fun initArguments() {
        arguments?.id?.let {
            viewModel.getUser(it)
        }
    }

    private fun handleSuccess(user: User?) {
        hideDialogLoading()
        this.user = user
        binding.tvDetails.text = user.toString()
    }

    fun onBackPressed() {
        Intent().also { intent ->
            Bundle().also { bundle ->
                bundle.user = user
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
