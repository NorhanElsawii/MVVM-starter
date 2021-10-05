package com.starter.mvvm.utils.extensions

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

fun Fragment.replaceFragment(fragment: Fragment, frameId: Int) {
    childFragmentManager.inTransaction { replace(frameId, fragment) }
}

inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction?) {
    beginTransaction().func()?.commit()
}