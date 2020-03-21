package com.dci.dev.appinfobadge.utils

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.provider.Settings
import android.webkit.WebView
import androidx.core.content.res.ResourcesCompat

/**
 * Extension method to find a device height in pixels
 */
inline val Context.displayHeight: Int
    get() = resources.displayMetrics.heightPixels

fun Context.goToSettings() {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    val uri = Uri.fromParts("package", packageName, null)
    intent.data = uri
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    startActivity(intent)
}

/**
 * Converts dp unit to equivalent pixels, depending on device density.
 **/
val Int.dp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()

/**
 * Converts device specific pixels to density independent pixels.
 **/
val Int.px: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

// Assets are hosted under http(s)://appassets.androidplatform.net/assets/... .
// If the application's assets are in the "main/assets" folder this will read the file
// from "main/assets/www/index.html" and load it as if it were hosted on:
// https://appassets.androidplatform.net/assets/www/index.html
fun WebView.loadAsset(file: String, context: Context, darkMode: Boolean) {
    this.webViewClient = AssetsWebViewClient(context, darkMode)
    loadUrl("https://appassets.androidplatform.net/assets/$file")
}

fun Context.resolveColor(colorRes: Int) =
    ResourcesCompat.getColor(resources, colorRes, null)

fun Context.resolveDrawable(drawableRes: Int) =
    ResourcesCompat.getDrawable(resources, drawableRes, null)
