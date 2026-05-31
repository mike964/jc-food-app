package com.example.gmfastfood.home

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun FilterButton(title: String, selected: Boolean, onClick: () -> Unit   ){

//    var selected_ by remember { mutableStateOf(selected) }

    Button(
//        onClick = {  selected_ = !selected_     },
        onClick = { onClick()   },
     //   modifier = Modifier.padding(horizontal = 4.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selected) Color(0xFFA3ECAE) else  Color(0xFFECF3F2), // Background color
            contentColor = Color.DarkGray   // Text/Icon color
        )
    ) {
        Text(title)
    }
}