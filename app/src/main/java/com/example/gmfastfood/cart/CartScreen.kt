package com.example.gmfastfood.cart

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.gmfastfood.vm.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    viewModel: CartViewModel
) {
    // Safely collect state from ViewModel
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Your Cart") })
        },
        containerColor = Color(0xFFA8CCEE) // Set your custom color here
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Spacer(Modifier.height(8.dp))
            if (uiState.cartItems.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Your cart is empty", style = MaterialTheme.typography.bodyLarge)
                }
            } else {
                // 1. List of Items
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.cartItems, key = { it.id }) { item ->
                        CartItemRow(
                            item = item,
                            onIncrease = { viewModel.updateQuantity(item.id, item.quantity + 1) },
                            onDecrease = { viewModel.updateQuantity(item.id, item.quantity - 1) },
                            onRemove = { viewModel.removeItem(item.id) }
                        )
                    }
                }

                // 2. Summary Block at the bottom
                CartSummaryBlock(
                    subtotal = uiState.subtotal,
                    shipping = uiState.shipping,
                    total = uiState.total,
                    onCheckoutClick = { /* Handle Checkout Logic */ }
                )
            }
        }
    }
}

