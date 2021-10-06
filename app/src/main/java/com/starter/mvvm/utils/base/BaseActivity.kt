package com.starter.mvvm.utils.base

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.starter.mvvm.utils.locale.LocaleHelper


/**
 * Created by Norhan Elsawi on 10/04/2021.
 */
abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

    // Variables
    private var _binding: VB? = null

    // Binding variable to be used for accessing views.
    protected val binding
        get() = requireNotNull(_binding)

    abstract fun onViewCreated()

    abstract fun setupViewBinding(inflater: LayoutInflater): VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = setupViewBinding(layoutInflater)
        setContentView(requireNotNull(_binding).root)
        onViewCreated()
        overridePendingTransition(getStartAnimation(), android.R.anim.fade_out)
    }

    open fun getStartAnimation() = android.R.anim.fade_in

    open fun getEndAnimation() = android.R.anim.fade_out

    override fun onPause() {
        super.onPause()
        if (isFinishing)
            overridePendingTransition(0, getEndAnimation())
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase))
    }

    override fun applyOverrideConfiguration(overrideConfiguration: Configuration?) {
        super.applyOverrideConfiguration(LocaleHelper.onAttach(baseContext).resources.configuration)
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}