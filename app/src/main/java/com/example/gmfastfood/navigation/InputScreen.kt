package com.example.gmfastfood.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier

@Composable
fun InputScreen(viewModel: SharedNavigationViewModel) {
    val text by viewModel.sharedText.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
    ) {
        Text(text = "Tab 1: Type something below", style = MaterialTheme.typography.titleMedium)
//        Spacer(modifier = Modifier.height(16.png))
        TextField(
            value = text,
            onValueChange = { viewModel.updateText(it) },
            label = { Text("Shared Data") }
        )
    }
}

@Composable
fun DisplayScreen(viewModel: SharedNavigationViewModel) {
    val text by viewModel.sharedText.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
    ) {
        Text(text = "Tab 2: Retrieved Data", style = MaterialTheme.typography.titleMedium)
//        Spacer(modifier = Modifier.height(16.png))
        Text(
            text = text.ifEmpty { "No data entered yet." },
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary
        )
    }
}