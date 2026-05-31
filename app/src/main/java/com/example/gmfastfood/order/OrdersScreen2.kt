package com.example.gmfastfood.order

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowForwardIos
//import androidx.compose.material.icons.filled.LocalMall
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.text.NumberFormat
import java.util.Locale

// 1. Data Models
data class OrderItem_(
    val id: String,
    val restaurantOrStoreName: String,
    val itemsSummary: String,
    val totalPrice: Double,
    val status: OrderStatus_,
    val dateString: String,
)

enum class OrderStatus_ {
    PLACED, PREPARING, OUT_FOR_DELIVERY, DELIVERED, CANCELLED;

    val label: String get() = name.replace("_", " ").lowercase().replaceFirstChar { it.uppercase() }
}

// 2. Main Screen Container
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersScreen2(
//    orders: List<OrderItem_>,
    onOrderClick: (String) -> Unit,
    onTrackOrderClick: (String) -> Unit,
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Active", "Past Orders")

    val orders = remember {
        mutableStateListOf<OrderItem_>(
            OrderItem_(
                id = "1", "", "", 15.50, OrderStatus_.DELIVERED, ""
            )
        )
    }

    // Separate data streams based on selection
    val filteredOrders = remember(selectedTab, orders) {
        if (selectedTab == 0) {
            orders.filter { it.status != OrderStatus_.DELIVERED && it.status != OrderStatus_.CANCELLED }
        } else {
            orders.filter { it.status == OrderStatus_.DELIVERED || it.status == OrderStatus_.CANCELLED }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Orders", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Text("Orders Screen - Example 2")
            // Tab Header Layout
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title, fontWeight = FontWeight.Medium) }
                    )
                }
            }

            // Order Content List
            if (filteredOrders.isEmpty()) {
                EmptyOrdersState()
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredOrders, key = { it.id }) { order ->
                        OrderCard(
                            order = order,
                            onClick = { onOrderClick(order.id) },
                            onActionClick = { onTrackOrderClick(order.id) }
                        )
                    }
                }
            }
        }
    }
}

// 3. Individual Order Card Component
@Composable
fun OrderCard(
    order: OrderItem_,
    onClick: () -> Unit,
    onActionClick: () -> Unit,
) {
    val currencyFormatter = remember { NumberFormat.getCurrencyInstance(Locale.US) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = order.restaurantOrStoreName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = order.dateString,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                StatusBadge_(status = order.status)
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = order.itemsSummary,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Total Amount",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = currencyFormatter.format(order.totalPrice),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                // Contextual primary actions based on active state vs history
                if (order.status != OrderStatus_.DELIVERED && order.status != OrderStatus_.CANCELLED) {
                    Button(
                        onClick = onActionClick,
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text("Track Order")
                    }
                } else {
                    OutlinedButton(
                        onClick = onActionClick,
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text("Reorder")
                    }
                }
            }
        }
    }
}

// 4. Dynamic Status Indicator Badge
@Composable
fun StatusBadge_(status: OrderStatus_) {
    val (backgroundColor, contentColor) = when (status) {
        OrderStatus_.PLACED -> MaterialTheme.colorScheme.secondaryContainer to MaterialTheme.colorScheme.onSecondaryContainer
        OrderStatus_.PREPARING -> MaterialTheme.colorScheme.tertiaryContainer to MaterialTheme.colorScheme.onTertiaryContainer
        OrderStatus_.OUT_FOR_DELIVERY -> Color(0xFFFFF3E0) to Color(0xFFE65100) // Custom warm orange accent
        OrderStatus_.DELIVERED -> Color(0xFFE8F5E9) to Color(0xFF2E7D32) // Soft safe green
        OrderStatus_.CANCELLED -> MaterialTheme.colorScheme.errorContainer to MaterialTheme.colorScheme.onErrorContainer
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = status.label,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            color = contentColor
        )
    }
}

// 5. Fallback Placeholder for Empty Views
@Composable
fun EmptyOrdersState() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
//        Icon(
//            imageVector = Icons.Default.LocalMall,
//            contentDescription = null,
//            modifier = Modifier.size(64.dp),
//            tint = MaterialTheme.colorScheme.outline
//        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No orders found",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "When you place orders, they will show up here details and tracking metrics.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}