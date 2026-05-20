package com.example.gmfastfood

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun HorizontalSlider() {
    val texts = listOf<SlidingContent>(
        SlidingContent("All", true),
        SlidingContent("Combos", false),
        SlidingContent("Sliders", false),
        SlidingContent("Classics", false)
    )
    Row(
        modifier = Modifier
            .padding(horizontal = 11.dp, vertical = 20.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        texts.forEach {
            Box(
                modifier = Modifier
                    .height(40.dp)
                    .wrapContentWidth()
                    .background(
                        if (it.selected) Color(0xffEF2A39) else Color(0xffF3F4F6),
                        RoundedCornerShape(20.dp)
                    )
                    .padding(horizontal = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    it.text,
                    Modifier,
                    color = if (it.selected) Color.White else Color(0xff6a6a6a),
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}