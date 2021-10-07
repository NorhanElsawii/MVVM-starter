package com.starter.mvvm.utils.base

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.starter.mvvm.databinding.ActivityCommonBinding
import com.starter.mvvm.utils.locale.LocaleHelper
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by Norhan Elsawi on 10/04/2021.
 */
@AndroidEntryPoint
abstract class BaseActivity : AppCompatActivity() {

    // Variables
    private var _binding: ActivityCommonBinding? = null

    // Binding variable to be used for accessing views.
    protected val binding
        get() = requireNotNull(_binding)

    abstract fun onViewCreated()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityCommonBinding.inflate(layoutInflater)
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
