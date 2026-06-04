package com.example.gmfastfood.map

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@SuppressLint("JavascriptInterface", "SetJavaScriptEnabled") // Required when adding JS interface
@Composable
fun LeafletMap(
    startingLocation : String,
    onLocationSelected: (Double, Double) -> Unit,
    modifier: Modifier = Modifier,
) {

    val fileName = "leaflet_map.html"

    AndroidView(
        modifier = modifier.fillMaxSize(),
        factory = { context ->
            WebView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )

                webViewClient = WebViewClient()
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true

                // --- Attach the Bridge Here ---
                addJavascriptInterface(
                    WebAppInterface(onLocationSelected),
                    "AndroidBridge" // The variable name available in JS
                )
//                loadUrl("file:///android_asset/leaflet_map.html")
            }
        },
        update = { webView ->
            // Use the standard URL prefix for android assets
            webView.loadUrl("file:///android_asset/$fileName")
        }
    )
}

@Composable
fun LeafletMapExample (){
    var clickedLocation by remember { mutableStateOf("No location clicked yet") }
    val startingLocation = "37.7749,-122.4194"

    Column {
        Text(text = "Selected: $clickedLocation")

        LeafletMap(
            startingLocation ,
            onLocationSelected = { lat, lng ->
                // This updates the Compose state!
                clickedLocation = "Lat: $lat, Lng: $lng"
            }
        )
    }
}

