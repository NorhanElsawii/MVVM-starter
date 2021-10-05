package com.starter.mvvm.utils.base

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by Norhan Elsawi on 4/10/2021.
 */
@AndroidEntryPoint
abstract class BaseFragment : Fragment() {

    private var pd: Dialog? = null
    private var baseViewModel: BaseViewModel? = null

    abstract fun layoutId(): Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View =
        inflater.inflate(layoutId(), container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }
}