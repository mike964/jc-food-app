package com.example.gmfastfood.map

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.viewinterop.AndroidView

// # Load local HTML file from assets folder in android studio
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun LocalHtmlWebView(fileName: String, modifier: Modifier = Modifier) {
    AndroidView(
        modifier = modifier.fillMaxSize(),
        factory = { context ->
            WebView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                // Set up a basic WebViewClient
                webViewClient = WebViewClient()

                // Optional: Enable JavaScript if your asset uses it
                settings.javaScriptEnabled = true
            }
        },
        update = { webView ->
            // Use the standard URL prefix for android assets
            webView.loadUrl("file:///android_asset/$fileName")
        }
    )
}

// #  Rendering Basic HTML Styling without WebView
@Composable
fun NativeHtmlTextFromAssets(fileName: String) {
    val context = LocalContext.current

    // Read the text file from assets folder safely
    val htmlString = remember(fileName) {
        context.assets.open(fileName).bufferedReader().use { it.readText() }
    }

    // Displays parsed HTML inside a standard Compose Text view
    Text(text = AnnotatedString.fromHtml(htmlString))
}