package com.example.gmfastfood

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

@Composable
fun StickyRowExample() {
    val scrollState = rememberScrollState()

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // 1. SCROLLING CONTENT
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(top = 60.dp) // Offset by the row's height
            ) {
                // Add your scrolling items here
                repeat(50) { index ->
                    Text(
                        text = "Item #$index",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            // 2. STICKY/PINNED ROW
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .background(MaterialTheme.colorScheme.surface)
                    .zIndex(1f), // Keeps the row on top
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Pinned Row Content", modifier = Modifier.padding(16.dp))
            }
        }
    }
}