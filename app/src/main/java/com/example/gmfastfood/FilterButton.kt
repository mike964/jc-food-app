package com.example.gmfastfood

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun FilterButton(title: String, selected: Boolean, onClick: () -> Unit   ){

    var selected_ by remember { mutableStateOf(selected) }

    Button(
        onClick = {  selected_ = !selected_     },
     //   modifier = Modifier.padding(horizontal = 4.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selected_) Color(0xFFBEE8C0) else  Color(0xFFEAEBEE), // Background color
            contentColor = Color.DarkGray   // Text/Icon color
        )
    ) {
        Text(title)
    }
}