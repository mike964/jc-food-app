package com.example.gmfastfood.map

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun LeafletMapScreen(
    latitude: Double?,
    longitude: Double?,
    zoomLevel: Int = 13,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    var webViewInstance by remember { mutableStateOf<WebView?>(null) }
    var isMapLoaded by remember { mutableStateOf(false) }

    // Minimal self-contained template containing Leaflet scripts, styles, and dynamic JS entry points
    val leafletHtml = remember {
        """
        <!DOCTYPE html>
        <html>
        <head>
            <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
            <link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css" />
            <style>
                html, body, #map { height: 100%; width: 100%; margin: 0; padding: 0; }
            </style>
            <script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>
        </head>
        <body>
            <div id="map"></div>
            <script>
                var map;
                var currentMarker;

                function initMap(lat, lng, zoom) {
                    // Initialize Map instance hooked to our div container
                    map = L.map('map').setView([lat, lng], zoom);
                    
                    // Add OpenStreetMap Standard Tile Layer
                    L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
                        maxZoom: 19,
                        attribution: '&copy; OpenStreetMap contributors'
                    }).addTo(map);

                    // Drop primary starting point marker
                    currentMarker = L.marker([lat, lng]).addTo(map);
                }

                function updateLocation(lat, lng, zoom) {
                    if (map) {
                        // Smoothly transition view window to target coordinates
                        map.setView([lat, lng], zoom);
                        
                        // Shift marker position safely
                        if (currentMarker) {
                            currentMarker.setLatLng([lat, lng]);
                        } else {
                            currentMarker = L.marker([lat, lng]).addTo(map);
                        }
                    }
                }
            </script>
        </body>
        </html>
        """.trimIndent()
    }

    // React to Compose State Coordinate changes safely
    LaunchedEffect(latitude, longitude, zoomLevel, isMapLoaded) {
        if (isMapLoaded) {
            // Evaluates update script on the active WebView DOM context
            webViewInstance?.evaluateJavascript(
                "updateLocation($latitude, $longitude, $zoomLevel);",
                null
            )
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                WebView(context).apply {
                    // The 3 lines below are essential for maps to display
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )

                    webViewClient = object : WebViewClient() {
                        override fun onPageFinished(view: WebView?, url: String?) {
                            super.onPageFinished(view, url)
                            // Initialize the map with starting coordinates once DOM finishes loading
                            evaluateJavascript("initMap($latitude, $longitude, $zoomLevel);", null)
                            isMapLoaded = true
                        }
                    }

                    settings.apply {
                        javaScriptEnabled = true
                        domStorageEnabled = true
                        loadWithOverviewMode = true
                        useWideViewPort = true
                    }

                    // Feed structured string layout data safely
//                   loadDataWithBaseURL("https://localhost", "file:///android_asset/leaflet_map2", "text/html", "UTF-8", null)
                    loadDataWithBaseURL("https://localhost", leafletHtml, "text/html", "UTF-8", null)
                     webViewInstance = this
                }
            },
            update = {
                // Kept intentionally vacant; updates are smoothly streamlined by LaunchedEffect
            }
        )
    }
}

@Composable
fun MapContainerDemo() {
    // Scaffold Default Points: Paris
    var currentLat by remember { mutableDoubleStateOf(48.8566) }
    var currentLng by remember { mutableDoubleStateOf(2.3522) }

    Column(modifier = Modifier.fillMaxSize()) {
        // Map viewport consuming the bulk allocation space
        LeafletMapScreen(
            latitude = currentLat,
            longitude = currentLng,
            zoomLevel = 12,
            modifier = Modifier.weight(1f)
        )

        // Switch Controller Buttons row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = {
                currentLat = 48.8566
                currentLng = 2.3522
            }) {
                Text("Paris")
            }

            Button(onClick = {
                currentLat = 40.7128
                currentLng = -74.0060
            }) {
                Text("New York")
            }

            Button(onClick = {
                currentLat =  32.59665920995979
                currentLng = 44.02086555027
            }) {
                Text("Karbala")
            }
        }
    }
}