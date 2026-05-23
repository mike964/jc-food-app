package com.example.gmfastfood.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    onBackClicked: () -> Unit = {},
    onOrderPlaced: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Checkout", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClicked) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Go Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFEFF2F5))
        ) {
            // Scrollable Content Region
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Spacer(modifier = Modifier.height(4.dp))

                // 1. Shipping Section
                CheckoutSectionCard(
                    title = "Shipping Address",
                    icon = Icons.Default.LocationOn,
                    actionText = "Change"
                ) {
                    Text("Alex Mercer", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                    Text("123 Android Way, Suite 404", color = Color.Gray, fontSize = 13.sp)
                    Text("Brooklyn, NY 11201", color = Color.Gray, fontSize = 13.sp)
                }

                // 2. Payment Section
                CheckoutSectionCard(
                    title = "Payment Method",
                    icon = Icons.Default.CheckCircle,
                    actionText = "Edit"
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("💳", fontSize = 20.sp, modifier = Modifier.padding(end = 8.dp))
                        Column {
                            Text("Mastercard ending in 4321", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                            Text("Expires 12/29", color = Color.Gray, fontSize = 12.sp)
                        }
                    }
                }

                // 3. Summary Breakdown Card
                OrderSummaryCard(
                    subtotal = 1184.48,
                    shipping = 0.00,
                    tax = 94.75
                )

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Fixed Sticky Bottom Action bar for conversion optimization
            Surface(
                modifier = Modifier.fillMaxWidth(),
                tonalElevation = 8.dp,
                shadowElevation = 16.dp,
                color = Color.White
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Total Due:", fontSize = 14.sp, color = Color.Gray)
                        Text("$1,279.23", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
                    }

                    Button(
                        onClick = onOrderPlaced,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Place Order", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
            }
        }
    }
}

// ==========================================
// REUSABLE SUB-COMPONENTS
// ==========================================

@Composable
fun CheckoutSectionCard(
    title: String,
    icon: ImageVector,
    actionText: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
                TextButton(onClick = { /* Handle Edit Action */ }) {
                    Text(actionText, fontSize = 13.sp)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            content()
        }
    }
}

@Composable
fun OrderSummaryCard(subtotal: Double, shipping: Double, tax: Double) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text("Order Summary", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            HorizontalDivider(color = Color(0xFFF1F1F1))

            SummaryRow(label = "Subtotal", value = "$$subtotal")
            SummaryRow(label = "Shipping", value = if (shipping == 0.0) "FREE" else "$$shipping", valueColor = if (shipping == 0.0) Color(0xFF2E7D32) else Color.Black)
//            SummaryRow(label = "Estimated Tax", value = "$$tax")
        }
    }
}

@Composable
fun SummaryRow(label: String, value: String, valueColor: Color = Color.Unspecified) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = Color.Gray, fontSize = 14.sp)
        Text(value, fontWeight = FontWeight.Medium, fontSize = 14.sp, color = valueColor)
    }
}