package com.starter.mvvm.utils.extensions

import android.app.DatePickerDialog
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.facebook.shimmer.ShimmerFrameLayout
import com.starter.mvvm.R
import java.util.*

/**
 * Created by Norhan Elsawi on 23/01/2020.
 */
fun ImageView.loadImage(
    path: String?,
    @DrawableRes placeHolder: Int = R.drawable.ic_place_holder,
    success: () -> Unit = {},
    error: () -> Unit = {},
    setScale: Boolean = true,
) {
    if (setScale)
        this.scaleType = ImageView.ScaleType.FIT_XY

    if (path.isNullOrEmpty())
        setImageResource(placeHolder)
    else {
        getRequestBuilder(context, path, placeHolder, success, error)
            .error(getRequestBuilder(context, path, placeHolder, success, error))
            .into(this)
    }
}

private fun getRequestBuilder(
    context: Context,
    path: String?,
    @DrawableRes placeHolder: Int = R.drawable.ic_place_holder,
    success: () -> Unit = {},
    error: () -> Unit = {},
) = Glide.with(context)
    .load(path)
    .placeholder(
        context.getDrawableCompat(
            placeHolder
        )
    ).thumbnail(0.05f)
    .listener(object : RequestListener<Drawable> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: com.bumptech.glide.request.target.Target<Drawable>?,
            isFirstResource: Boolean,
        ): Boolean {
            error()
            return false
        }

        override fun onResourceReady(
            resource: Drawable?,
            model: Any?,
            target: com.bumptech.glide.request.target.Target<Drawable>?,
            dataSource: com.bumptech.glide.load.DataSource?,
            isFirstResource: Boolean,
        ): Boolean {
            success()
            return false
        }
    })

fun View.toggleCellVisibility(isVisible: Boolean) {
    if (isVisible) {
        this.visibility = View.VISIBLE
        this.layoutParams = RecyclerView.LayoutParams(
            RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )
    } else {
        this.visibility = View.GONE
        this.layoutParams = RecyclerView.LayoutParams(0, 0)
    }
}

fun ShimmerFrameLayout.showShimmerView() {
    this.visibility = View.VISIBLE
    this.startShimmer()
}

fun ShimmerFrameLayout.hideShimmerView() {
    this.visibility = View.GONE
    this.stopShimmer()
}

fun View.deactivateView() {
    this.alpha = 0.6f
    this.isEnabled = false
}

fun View.activateView() {
    this.alpha = 1f
    this.isEnabled = true
}

fun EditText.showCalender(
    context: Context,
    initialDate: Long,
    onDateSelected: (date: Long) -> Unit,
) {
    val calendar = Calendar.getInstance()
    if (initialDate != 0L)
        calendar.timeInMillis = initialDate

    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val month = calendar.get(Calendar.MONTH)
    val year = calendar.get(Calendar.YEAR)

    DatePickerDialog(
        context,
        DatePickerDialog.OnDateSetListener { _, selectedYear, monthOfYear, dayOfMonth ->
            Calendar.getInstance().also {
                it.set(Calendar.YEAR, selectedYear)
                it.set(Calendar.MONTH, monthOfYear)
                it.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                onDateSelected(it.timeInMillis)
                this.setText(it.timeInMillis.toDate())
            }
        },
        year, month, day
    ).also {
        it.datePicker.maxDate = System.currentTimeMillis()
        it.show()
    }
}
