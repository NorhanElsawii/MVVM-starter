package com.starter.mvvm.utils.base

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.starter.mvvm.R
import com.starter.mvvm.data.remote.ErrorResponse
import com.starter.mvvm.databinding.LayoutAlerterBinding
import com.starter.mvvm.utils.Status
import com.starter.mvvm.utils.extensions.hideSoftKeyboard
import com.starter.mvvm.utils.extensions.observe
import com.starter.mvvm.utils.extensions.showSoftKeyboard
import com.tapadoo.alerter.Alerter

/**
 * Created by Norhan Elsawi on 4/10/2021.
 */
typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

abstract class BaseFragment<VB : ViewBinding>(
    private val inflate: Inflate<VB>,
) : Fragment() {

    private var _binding: VB? = null

    // Binding variable to be used for accessing views.
    protected val binding
        get() = requireNotNull(_binding)

    private lateinit var pd: Dialog

    abstract fun getCurrentViewModel(): BaseViewModel?

    abstract fun onViewReady()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = inflate.invoke(inflater, container, false)
        return requireNotNull(_binding).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        createCustomProgressDialog()
        onViewReady()
        initListeners()
    }

    private fun initListeners() {
        observe(getCurrentViewModel()?.showLoginDialog) {
            showLoginDialog()
        }
        observe(getCurrentViewModel()?.showNetworkError) {
            showErrorMsg(getString(R.string.check_internet_connection))
        }
    }

    fun showErrorMsg(msg: String?) {
        if (!msg.isNullOrEmpty())
            initAlerter {
                initAlerterView(
                    msg,
                    R.drawable.ic_cancel,
                    R.color.transparent_red,
                    it
                )
            }
    }

    fun showMsg(msg: String?) {
        if (!msg.isNullOrEmpty())
            initAlerter {
                initAlerterView(
                    msg,
                    R.drawable.ic_check,
                    R.color.transparent_green,
                    it
                )
            }
    }

    private fun initAlerter(onViewIsReady: (view: View?) -> Unit) {
        Alerter.create(requireActivity(), R.layout.layout_alerter)
            .setBackgroundColorRes(android.R.color.transparent)
            .also { alerter ->
                onViewIsReady.invoke(alerter.getLayoutContainer())
            }
            .setTextAppearance(R.style.AlerterCustomTextAppearance)
            .setDuration(3000)
            .enableSwipeToDismiss()
            .show()
    }

    private fun initAlerterView(msg: String, icon: Int, background: Int, view: View?) {
        view?.let {
            with(LayoutAlerterBinding.bind(it)) {
                tvMsg.text = msg
                llContainer.setBackgroundResource(background)
                ivIcon.setImageResource(icon)
            }
        }
    }

    private fun createCustomProgressDialog() {
        pd = Dialog(requireContext(), R.style.DialogCustomTheme)
        pd.setContentView(R.layout.layout_dialog_progress)
        pd.setCancelable(false)
    }

    fun showDialogLoading() {
        if (!pd.isShowing)
            pd.show()
    }

    fun hideDialogLoading() {
        if (pd.isShowing)
            pd.dismiss()
    }

    fun hideSoftKeyboard() {
        activity.hideSoftKeyboard()
    }

    fun showSoftKeyboard() {
        activity.showSoftKeyboard()
    }

    private fun showLoginDialog() {
        // pass
    }

    fun <E> onError(error: Status.Error<E>, showErrorMsg: Boolean = true, handleError: () -> Unit) {
        handleErrorStatus(error.errorResponse, showErrorMsg, handleError)
    }

    fun <E> onError(
        error: Status.ErrorLoadingMore<E>,
        showErrorMsg: Boolean = true,
        handleError: () -> Unit,
    ) {
        handleErrorStatus(error.errorResponse, showErrorMsg, handleError)
    }

    private fun <E> handleErrorStatus(
        errorResponse: ErrorResponse<E>,
        showErrorMsg: Boolean = true,
        handleError: () -> Unit,
    ) {
        if (errorResponse.code == 426)
            handleForceUpdate()
        else if (showErrorMsg)
            showErrorMsg(errorResponse.message)

        if (!errorResponse.showOnlyErrorMsg)
            handleError.invoke()
    }

    private fun handleForceUpdate() {
        // pass
    }

    private fun handleUnAuthorizedUser(code: Int) {
        //        if (baseViewModel?.getUser() != null && code == 401) {
//            baseViewModel?.clearUserData()
//            Handler().postDelayed({
//                LoginActivity.start(activity)
//            }, 2000)
//        }
    }

    override fun onStop() {
        super.onStop()
        Alerter.clearCurrent(activity)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
