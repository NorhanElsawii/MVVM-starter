package com.starter.mvvm.utils.extensions

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.content.pm.PackageInfoCompat
import com.starter.mvvm.utils.Constants


/**
 * Created by Norhan Elsawi on 4/10/2021.
 */
fun Context.getColorCompat(@ColorRes color: Int): Int {
    return ContextCompat.getColor(this, color)
}

fun Context.getDrawableCompat(@DrawableRes drawable: Int): Drawable? {
    return ContextCompat.getDrawable(this, drawable)
}

fun Context.getSharedPref(): SharedPreferences {
    return this.getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE)
}

fun Context.getVersionCode(): String {
    try {
        val pInfo = packageManager.getPackageInfo(packageName, 0)
        val longVersionCode = PackageInfoCompat.getLongVersionCode(pInfo)
        return "$longVersionCode.0"
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return ""
}

fun Context.openGooglePlayStoreForUpdate() {
    val appPackageName = packageName
    try {
        val intent = Intent(
            Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName")
        )
        intent.setPackage("com.android.vending")
        startActivity(intent)
    } catch (e: Exception) {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
            )
        )
    }
}

fun Context.isRtl(): Boolean {
    val config = this.resources.configuration
    return config.layoutDirection == View.LAYOUT_DIRECTION_RTL
}

fun Context.getDimension(dim: Int): Int {
    return this.resources.getDimension(dim).toInt()
}