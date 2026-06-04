package com.example.gmfastfood.map

import android.annotation.SuppressLint
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@SuppressLint("JavascriptInterface", "SetJavaScriptEnabled") // Required when adding JS interface
@Composable
fun LeafletMap(
    onLocationSelected: (Double, Double) -> Unit,
    modifier: Modifier = Modifier
) {
    AndroidView(
        modifier = modifier.fillMaxSize(),
        factory = { context ->
            WebView(context).apply {
                webViewClient = WebViewClient()
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true

                // --- Attach the Bridge Here ---
                addJavascriptInterface(
                    WebAppInterface(onLocationSelected),
                    "AndroidBridge" // The variable name available in JS
                )

                loadUrl("file:///android_asset/leaflet_map.html")
            }
        }
    )
}