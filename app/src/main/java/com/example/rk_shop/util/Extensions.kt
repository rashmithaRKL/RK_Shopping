package com.example.rk_shop.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.snackbar.Snackbar
import java.io.File
import java.io.FileOutputStream
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

// Context Extensions
fun Context.toast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

fun Context.getColorCompat(colorRes: Int) = ContextCompat.getColor(this, colorRes)
fun Context.getDrawableCompat(drawableRes: Int) = ContextCompat.getDrawable(this, drawableRes)

// View Extensions
fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.showSnackbar(
    message: String,
    duration: Int = Snackbar.LENGTH_SHORT,
    action: String? = null,
    actionCallback: (() -> Unit)? = null
) {
    Snackbar.make(this, message, duration).apply {
        action?.let { actionText ->
            setAction(actionText) { actionCallback?.invoke() }
        }
    }.show()
}

// Activity Extensions
fun Activity.hideKeyboard() {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    currentFocus?.let {
        imm.hideSoftInputFromWindow(it.windowToken, 0)
    }
}

inline fun <reified T : Activity> Activity.startActivityWithAnimation(
    noinline init: Intent.() -> Unit = {}
) {
    val intent = Intent(this, T::class.java)
    intent.init()
    startActivity(intent)
    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
}

// Fragment Extensions
fun Fragment.hideKeyboard() {
    activity?.hideKeyboard()
}

// LiveData Extensions
fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
    observe(lifecycleOwner, object : Observer<T> {
        override fun onChanged(t: T) {
            observer.onChanged(t)
            removeObserver(this)
        }
    })
}

// ImageView Extensions
fun ImageView.loadImage(
    url: String?,
    @DrawableRes placeholder: Int? = null,
    @DrawableRes error: Int? = null
) {
    Glide.with(context)
        .load(url)
        .apply {
            placeholder?.let { placeholder(it) }
            error?.let { error(it) }
        }
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)
}

// String Extensions
fun String.toDate(pattern: String = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"): Date? {
    return try {
        SimpleDateFormat(pattern, Locale.getDefault()).parse(this)
    } catch (e: Exception) {
        null
    }
}

// Date Extensions
fun Date.format(pattern: String = "yyyy-MM-dd HH:mm"): String {
    return SimpleDateFormat(pattern, Locale.getDefault()).format(this)
}

// Number Extensions
fun BigDecimal.formatPrice(currency: String = "$"): String {
    return "$currency${this.setScale(2).toString()}"
}

fun Double.roundTo(decimals: Int): Double {
    val factor = Math.pow(10.0, decimals.toDouble())
    return (this * factor).roundToInt() / factor
}

// Bitmap Extensions
fun Bitmap.saveToFile(file: File): Boolean {
    return try {
        FileOutputStream(file).use { out ->
            this.compress(Bitmap.CompressFormat.JPEG, 100, out)
        }
        true
    } catch (e: Exception) {
        false
    }
}

fun File.toBitmap(): Bitmap? {
    return try {
        BitmapFactory.decodeFile(absolutePath)
    } catch (e: Exception) {
        null
    }
}

// Uri Extensions
fun Uri.toFile(context: Context): File? {
    return try {
        val inputStream = context.contentResolver.openInputStream(this)
        val file = File(context.cacheDir, "temp_${System.currentTimeMillis()}")
        inputStream?.use { input ->
            FileOutputStream(file).use { output ->
                input.copyTo(output)
            }
        }
        file
    } catch (e: Exception) {
        null
    }
}

// Bundle Extensions
fun Bundle.putAny(key: String, value: Any?) {
    when (value) {
        is String -> putString(key, value)
        is Int -> putInt(key, value)
        is Long -> putLong(key, value)
        is Boolean -> putBoolean(key, value)
        is Float -> putFloat(key, value)
        is Double -> putDouble(key, value)
        is Parcelable -> putParcelable(key, value)
        is Serializable -> putSerializable(key, value)
    }
}
