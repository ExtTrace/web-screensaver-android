package com.web.screensaver

import android.content.SharedPreferences
import android.service.dreams.DreamService
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient

/**
 * ClockDreamService - Renders a web URL as an Android screensaver (Daydream).
 * Exposes AndroidBridge.exitScreensaver() to JavaScript to allow UI-driven exit.
 */
class ClockDreamService : DreamService() {

    companion object {
        const val PREFS_NAME = "screensaver_prefs"
        const val PREF_URL = "screensaver_url"
        const val DEFAULT_URL = "https://exttrace.github.io/?mode=android"
    }

    private var webView: WebView? = null

    inner class AndroidBridge {
        @JavascriptInterface
        fun exitScreensaver() {
            finish()
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        // Allow user touch interaction (e.g. tap to change clock format)
        isInteractive = true

        // Run in full screen, hide navigation bar and status bar
        isFullscreen = true

        // Keep the screen alive during screensaver
        window?.addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        // Setup WebView to render the clock page
        webView = WebView(this).apply {
            settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
                cacheMode = WebSettings.LOAD_DEFAULT
                mixedContentMode = WebSettings.MIXED_CONTENT_NEVER_ALLOW
                useWideViewPort = true
                loadWithOverviewMode = true
                setSupportZoom(false)
                builtInZoomControls = false
                displayZoomControls = false
            }

            // Bind JavaScript bridge to allow exiting screensaver via web UI
            addJavascriptInterface(AndroidBridge(), "AndroidBridge")

            // Keep background black to match clock theme
            setBackgroundColor(android.graphics.Color.BLACK)

            // Hide system bars using modern API (API 30+) with legacy fallback
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                windowInsetsController?.let {
                    it.hide(android.view.WindowInsets.Type.systemBars())
                    it.systemBarsBehavior =
                        android.view.WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                }
            } else {
                @Suppress("DEPRECATION")
                systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                )
            }

            webViewClient = WebViewClient()

            // Load URL from SharedPreferences, fall back to default
            val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
            val url = prefs.getString(PREF_URL, DEFAULT_URL) ?: DEFAULT_URL
            loadUrl(url)
        }

        setContentView(webView)
    }

    override fun onDreamingStarted() {
        super.onDreamingStarted()
        webView?.onResume()
    }

    override fun onDreamingStopped() {
        super.onDreamingStopped()
        webView?.onPause()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        webView?.apply {
            stopLoading()
            destroy()
        }
        webView = null
    }
}
