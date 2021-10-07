package com.starter.mvvm.utils.extensions

import android.graphics.Color
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.starter.mvvm.data.remote.ErrorResponse
import com.starter.mvvm.data.remote.RemoteKeys.FILE
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File
import java.lang.reflect.Type
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Norhan Elsawi on 23/01/2020.
 */
fun Any?.toJsonString(): String = Gson().toJson(this)

fun <T> String?.toObjectFromJson(type: Type): T = Gson().fromJson(this, type)

fun <T : Any, L : LiveData<T>> LifecycleOwner.observe(liveData: L?, body: (T) -> Unit) =
    liveData?.observe(this, Observer(body))

// only error response read one Time, The second time you read return null
fun <D> Throwable.getErrorResponse(): ErrorResponse<D>? {
    return if (this is HttpException) {
        return try {
            response()?.errorBody()?.string()
                .toObjectFromJson<ErrorResponse<D>?>(ErrorResponse::class.java).also {
                    it?.code = code()
                }
        } catch (e: Exception) {
            null
        }
    } else
        null
}

fun Int.getFormattedNumberAccordingToLocal(): String {
    return String.format(Locale.getDefault(), "%d", this)
}

fun Float.getFormattedNumberAccordingToLocal(): String {
    return String.format(Locale.getDefault(), "%.1f", this)
}

fun Int.isColorDark(): Boolean {
    val darkness =
        1 - (0.299 * Color.red(this) + 0.587 * Color.green(this) + 0.114 * Color.blue(this)) / 255
    return darkness >= 0.5
}

fun Long.toTime(): String {
    val format = SimpleDateFormat("hh:mm a", Locale.getDefault())
    return format.format(this)
}

fun Long.toDate(): String {
    val format = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    return format.format(this)
}

fun Long.toDate2(): String {
    val format = SimpleDateFormat("dd-MM-yyyy | HH:mm", Locale.getDefault())
    return format.format(this)
}

fun Long.toDateWithMonthName(): String {
    val format = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
    return format.format(this)
}

fun Long.toDateWithMonthName2(): String {
    val format = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    return format.format(this)
}

fun Long.toDateSeparatedByNewLine(): String {
    val format = SimpleDateFormat("dd\nMMMM\nyyyy", Locale.getDefault())
    return format.format(this)
}

fun Long.getDuration(end: Long): String {
    val dateFormat = SimpleDateFormat("dd MMM", Locale.getDefault())
    return "${dateFormat.format(this)} - ${dateFormat.format(end)}"
}

fun File.getImageFilePart(): MultipartBody.Part {
    return MultipartBody.Part.createFormData(
        FILE,
        this.name,
        this.asRequestBody("image/*".toMediaType())
    )
}

fun String.getStringPart(): RequestBody {
    return this.toRequestBody("text/plain".toMediaType())
}

fun Float.round(decimalPlace: Int): Float {
    var bd = BigDecimal(this.toString())
    bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP)
    return bd.toFloat()
}

inline fun <reified T> List<T>.isEqual(second: List<T>): Boolean {
    if (this.size != second.size) {
        return false
    }
    return this.toTypedArray() contentEquals second.toTypedArray()
}
