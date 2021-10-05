package com.starter.mvvm.utils.base

import com.starter.mvvm.data.local.ConnectivityUtils
import com.starter.mvvm.data.local.LocalDataUtils
import javax.inject.Inject

abstract class BaseRepository {
    @Inject
    lateinit var connectivityUtils: ConnectivityUtils

    @Inject
    lateinit var localDataUtils: LocalDataUtils

    fun isNetworkConnected(): Boolean {
        return connectivityUtils.isConnectedToNetwork()
    }

    fun getString(id: Int): String {
        return localDataUtils.getString(id)
    }

    fun getCurrentLanguage() = localDataUtils.getLanguage()

    fun setLanguage(language: String) = localDataUtils.setLanguage(language)
}