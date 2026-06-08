package com.example.gmfastfood.checkout

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.gmfastfood.data.CartItem
import com.example.gmfastfood.data.Order
import com.example.gmfastfood.data.OrderItem
import com.example.gmfastfood.data.formatTimestamp

// # Show this after order placed successfully
// with button to go back to my orders
// --- Data Models ---


// --- Sample Data ---
val sampleOrder = Order(
    id = "#ORD-95821",
    createdAt = 1780325392000,
    status = "In Transit",
    items = listOf(
        CartItem(1, "Sandwich Super", "1", 6.99, null),
        CartItem(1, "Pizza ultra taste", "2", 15.49, null)
    ),
    subtotal = 279.98,
    shipping = 5.99,
    tax = 22.40,
    total = 308.37,
    address = "Dockland Street. House red",
    note = "Ring second floor"
)

// --- Main Composable ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailsScreen(
    order: Order?,
    onBackClick: () -> Unit,
    onHomeClick: () -> Unit,
    onMyOrdersClick: () -> Unit,
) {


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Order Details", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (order == null) {
            Text("No order found")
            return@Scaffold
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Success message
            item {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .background(Color.Green, shape = RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "Order placed successfully!",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
            // 1. Header Info
            item { OrderHeader(order) }

            // 2. Status Tracker
            item { OrderStatusTracker(order.status) }

            // 3. Items List Header
            item {
                Text(
                    text = "Items Ordered",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            // 4. Itemized List
//            items(order.items) { item ->
//                OrderItemRow(item)
//            }

            // 5. Price Breakdown
            item { PriceSummaryCard(order) }

            // 6. Actions
            item {
                Button(
                    onClick = { onHomeClick() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
//                    Text("Track live shipment")
                    Text("Home Screen")
                }
                Spacer(Modifier.height(8.dp))
                Button(
                    onClick = { onMyOrdersClick() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Gray,       // Sets the background color
                        contentColor = Color.White        // Sets the text/icon color
                    )
                ) {
                    Text("My Orders")
                }
            }
        }
    }
}

@Composable
fun OrderHeader(order: Order) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = order.id,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = order.status,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Placed on ${formatTimestamp(order.createdAt , null  )}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun OrderStatusTracker(currentStatus: String) {
    // Simple visual status indicator
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        StatusStep(title = "Confirmed", isCompleted = true)
        StatusLine(isCompleted = true)
        StatusStep(title = "In Transit", isCompleted = true) // Matches sample data
        StatusLine(isCompleted = false)
        StatusStep(title = "Delivered", isCompleted = false)
    }
}

@Composable
fun StatusStep(title: String, isCompleted: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = if (isCompleted) MaterialTheme.colorScheme.primary else Color.LightGray,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
fun RowScope.StatusLine(isCompleted: Boolean) {
    Box(
        modifier = Modifier
            .weight(1f)
            .height(2.dp)
            .padding(horizontal = 8.dp)
            .background(if (isCompleted) MaterialTheme.colorScheme.primary else Color.LightGray)
    )
}

@SuppressLint("DefaultLocale")
@Composable
fun OrderItemRow(item: OrderItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "Qty: ${item.quantity}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
            Text(
                text = "$${String.format("%.2f", item.price * item.quantity)}",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun PriceSummaryCard(order: Order) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SummaryLine(label = "Subtotal", value = "$${String.format("%.2f", order.subtotal)}")
            SummaryLine(label = "Shipping", value = "$${String.format("%.2f", order.shipping)}")
            SummaryLine(label = "Tax", value = "$${String.format("%.2f", order.tax)}")
            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Total",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "$${String.format("%.2f", order.total)}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun SummaryLine(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
    }
}