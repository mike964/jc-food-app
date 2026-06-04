package com.example.gmfastfood.map

import android.annotation.SuppressLint
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun HtmlWebViewScreen() {
    val context = LocalContext.current

    // Track the WebView instance to handle back navigation
    var webViewInstance by remember { mutableStateOf<WebView?>(null) }

    // Sample HTML Content with embedded CSS and a JavaScript callback
    val htmlContent = """
        <!DOCTYPE html>
        <html>
        <head>
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <style>
                body {
                    font-family: sans-serif;
                    padding: 20px;
                    background-color: #f0f4f8;
                    color: #333;
                    text-align: center;
                }
                h1 { color: #6200EE; }
                button {
                    background-color: #6200EE;
                    color: white;
                    border: none;
                    padding: 10px 20px;
                    font-size: 16px;
                    border-radius: 4px;
                    cursor: pointer;
                    margin-top: 15px;
                }
            </style>
        </head>
        <body>
            <h1>Hello from Jetpack Compose HTML!</h1>
            <p>This is a local HTML string running inside an embedded WebView.</p>
            <button onclick="sendToAndroid()">Click to Talk to Android</button>

            <script>
                function sendToAndroid() {
                    // 'AndroidBridge' is the identifier we register in Kotlin
                    AndroidBridge.showToast("Message sent from HTML JavaScript!");
                }
            </script>
        </body>
        </html>
    """.trimIndent()

    // Handle Intercepted Back Button Behavior
    BackHandler(enabled = webViewInstance?.canGoBack() == true) {
        webViewInstance?.goBack()
    }

    // Embed the Android View inside Compose
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { ctx ->
            WebView(ctx).apply {
                // Configure basic WebView settings
                webViewClient = WebViewClient() // Forces links to open inside the app instead of a browser

                settings.apply {
                    javaScriptEnabled = true
                    domStorageEnabled = true
                    loadWithOverviewMode = true
                    useWideViewPort = true
                }

                // Map JavaScript functions to native Kotlin code
                addJavascriptInterface(object {
                    @JavascriptInterface
                    fun showToast(message: String) {
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    }
                }, "AndroidBridge") // Name used in JS script context

                // Track this instance for BackHandler
                webViewInstance = this
            }
        },
        update = { webView ->
            // Load the HTML content cleanly with BaseURL mapping to avoid encoding bugs
            webView.loadDataWithBaseURL(
                null,
                htmlContent,
                "text/html",
                "utf-8",
                null
            )
        }
    )
}