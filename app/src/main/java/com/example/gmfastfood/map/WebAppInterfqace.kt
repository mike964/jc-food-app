package com.example.gmfastfood.map

import android.webkit.JavascriptInterface

class WebAppInterface(private val onCoordinatesSelected: (Double, Double) -> Unit) {

    @JavascriptInterface
    fun sendCoordinatesToAndroid(lat: Double, lng: Double) {
        // This runs on a WebView background thread, so if you update UI,
        // ensure you pipe it back to the Main thread if necessary.
        onCoordinatesSelected(lat, lng)
    }
}