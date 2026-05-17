package com.example.gmfastfood

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.layout.offset
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SplashScreen(){
    val gradient = Brush.linearGradient(
        listOf(Color(0xffFF939B), Color(0xffEF2A39))
    )
    Box(modifier = Modifier.fillMaxSize().background(gradient),
        contentAlignment = Alignment.BottomCenter
    ){

        Text(
            "Food Go",
            Modifier.fillMaxSize().padding(top =200.dp),
            fontSize = 60.sp,
            color = Color.White,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )
        Row(
            modifier =Modifier,
            verticalAlignment = Alignment.Bottom
        ) {
            Image(
                painter = painterResource(R.drawable.burger2),
                contentDescription = "burger"
            )
            Image(
                painter = painterResource(R.drawable.burger),
                contentDescription = "burger",
                modifier = Modifier.offset(x = (-50).dp)
            )
        }
    }
}