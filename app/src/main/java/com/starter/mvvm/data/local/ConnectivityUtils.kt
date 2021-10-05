package com.starter.mvvm.data.local

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ConnectivityUtils @Inject constructor(@ApplicationContext private val context: Context) {

    @SuppressLint("MissingPermission")
    fun isConnectedToNetwork(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

        if (capabilities != null) {
            return true
        }
        // capabilities.hasTransport(NetworkCapabilities.) --> To check Network type (wifi/cellular/ethernet)
        return false
    }
}