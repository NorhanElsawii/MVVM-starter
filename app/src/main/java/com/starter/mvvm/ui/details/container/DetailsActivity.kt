package com.starter.mvvm.ui.details.container

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import com.starter.mvvm.R
import com.starter.mvvm.ui.details.DetailsFragment
import com.starter.mvvm.utils.base.BaseActivity
import com.starter.mvvm.utils.extensions.id
import com.starter.mvvm.utils.extensions.launchActivityForResult
import com.starter.mvvm.utils.extensions.replaceFragment

class DetailsActivity : BaseActivity() {

    private lateinit var fragment: DetailsFragment

    companion object {
        fun start(
            fragment: Fragment,
            id: Int,
            activityResultLauncher: ActivityResultLauncher<Intent>,
            finish: Boolean = false,
        ) {
            fragment.launchActivityForResult<DetailsActivity>(activityResultLauncher) {
                Bundle().also {
                    it.id = id
                    this.putExtras(it)
                }
            }

            if (finish)
                fragment.activity?.finish()
        }
    }

    override fun onViewCreated() {
        replaceFragment(
            DetailsFragment.getInstance(intent.extras).also {
                fragment = it
            },
            R.id.fl_container
        )
    }

    override fun onBackPressed() {
        fragment.onBackPressed()
    }
}
