package com.starter.mvvm.ui.main.container

import android.view.LayoutInflater
import com.starter.mvvm.R
import com.starter.mvvm.databinding.ActivityMainBinding
import com.starter.mvvm.ui.main.MainFragment
import com.starter.mvvm.utils.base.BaseActivity
import com.starter.mvvm.utils.extensions.replaceFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun setupViewBinding(inflater: LayoutInflater) = ActivityMainBinding.inflate(inflater)

    override fun onViewCreated() {
        replaceFragment(
            MainFragment(),
            R.id.fl_container
        )
    }
}