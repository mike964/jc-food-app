package com.example.gmfastfood.map

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun MapExample (){
    var clickedLocation by remember { mutableStateOf("No location clicked yet") }

    Column {
        Text(text = "Selected: $clickedLocation")

        LeafletMap(
            onLocationSelected = { lat, lng ->
                // This updates the Compose state!
                clickedLocation = "Lat: $lat, Lng: $lng"
            }
        )
    }
}

