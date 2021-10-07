package com.starter.mvvm.data.local.sharedprefs

import android.content.Context
import com.starter.mvvm.utils.extensions.getSharedPref
import com.starter.mvvm.utils.locale.LocaleLanguage

class SharedPrefsUtils private constructor(private val sharedPrefs: SharedPrefs) {

    fun getLanguage(): String {
        return sharedPrefs.getString(LANGUAGE, null)
            ?: LocaleLanguage.getDefaultLanguage()
    }

    fun setLanguage(language: String) {
        sharedPrefs.putString(LANGUAGE, language)
    }

    companion object {
        const val LANGUAGE = "language"

        private var sharedPrefsUtils: SharedPrefsUtils? = null
        fun getInstance(context: Context): SharedPrefsUtils {
            return sharedPrefsUtils ?: SharedPrefsUtils(SharedPrefs(context.getSharedPref())).also {
                sharedPrefsUtils = it
            }
        }
    }
}
