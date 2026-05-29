package com.example.gmfastfood.order

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun StatusBadge(status: OrderStatus) {
    val (containerColor, contentColor) = when (status) {
        OrderStatus.PENDING -> Color(0xFFFEF3C7) to Color(0xFFD97706)      // Amber/Yellow
        OrderStatus.SHIPPED -> Color(0xFFDBEAFE) to Color(0xFF2563EB)      // Blue
        OrderStatus.DELIVERED -> Color(0xFFD1FAE5) to Color(0xFF059669)    // Green
        OrderStatus.CANCELLED -> Color(0xFFFEE2E2) to Color(0xFFDC2626)    // Red
    }

    Surface(
        color = containerColor,
        contentColor = contentColor,
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = status.displayName,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}