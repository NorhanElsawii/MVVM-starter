package com.starter.mvvm.utils.extensions

import android.os.Bundle
import com.starter.mvvm.data.remote.entites.User


private const val EXTRA_ID = "EXTRA_ID"
var Bundle.id: Int
    get() = getInt(EXTRA_ID, -1)
    set(value) {
        putInt(EXTRA_ID, value)
    }

private const val EXTRA_USER = "EXTRA_USER"
var Bundle.user: User?
    get() = getString(EXTRA_USER).toObjectFromJson(User::class.java)
    set(value) {
        putString(EXTRA_USER, value.toJsonString())
    }





