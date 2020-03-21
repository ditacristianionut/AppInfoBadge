package com.dci.dev.appinfobadge.utils

import android.content.Context
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.webkit.WebViewAssetLoader

internal class AssetsWebViewClient(context: Context, private val darkMode: Boolean) : WebViewClient() {
        private val  assetLoader = WebViewAssetLoader.Builder()
            .addPathHandler("/assets/", WebViewAssetLoader.AssetsPathHandler(context))
            .addPathHandler("/res/", WebViewAssetLoader.ResourcesPathHandler(context))
            .build()

        override fun shouldInterceptRequest(
            view: WebView?,
            request: WebResourceRequest
        ): WebResourceResponse? {
            return assetLoader.shouldInterceptRequest(request.url)
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            if (darkMode) {
                view?.loadUrl(
                    "javascript:document.body.style.setProperty(\"color\", \"white\");"
                )
            } else {
                super.onPageFinished(view, url)
            }
        }
    }
