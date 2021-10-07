package com.starter.mvvm.utils.base

import com.starter.mvvm.data.local.LocalDataUtils
import javax.inject.Inject

/**
 * Created by Norhan Elsawi on 4/10/2021.
 */
abstract class BaseRepository {

    @Inject
    lateinit var localDataUtils: LocalDataUtils

    fun isNetworkConnected(): Boolean {
        return localDataUtils.isNetworkConnected()
    }

    fun getString(id: Int): String {
        return localDataUtils.getString(id)
    }

    fun getCurrentLanguage() = localDataUtils.getLanguage()

    fun setLanguage(language: String) = localDataUtils.setLanguage(language)
}
