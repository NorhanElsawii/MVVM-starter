package com.starter.mvvm.ui.main.container

import com.starter.mvvm.R
import com.starter.mvvm.ui.main.MainFragment
import com.starter.mvvm.utils.base.BaseActivity
import com.starter.mvvm.utils.extensions.replaceFragment

class MainActivity : BaseActivity() {

    override fun onViewCreated() {
        replaceFragment(
            MainFragment(),
            R.id.fl_container
        )
    }
}
