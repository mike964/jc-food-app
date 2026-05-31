package com.example.gmfastfood.cart

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ==========================================
// 1. DATA MODELS
// ==========================================
data class CartLineItem(
    val id: Int,
    val title: String,
    val price: Double,
    val initialQuantity: Int
)

// ==========================================
// 2. MAIN HOST SCREEN WITH SHEET STATE TRIGGER
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainCatalogScreenWithCart() {
    // Simulated Reactive State for Cart Items Array List
    var cartListState by remember {
        mutableStateOf(
            listOf(
                CartLineItem(1, "Wireless ANC Headphones", 129.99, 1),
                CartLineItem(2, "Flagship Phone 5G", 899.99, 2),
                CartLineItem(3, "Minimalist Smartwatch", 199.50, 1)
            )
        )
    }

    // Material 3 bottom sheet structural state variables
    var showCartSheet by remember { mutableStateOf(false) }
    val totalItemsCount = cartListState.sumOf { it.initialQuantity }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gadget Hub Market", fontWeight = FontWeight.Bold) },
                actions = {
                    // Tap navigation icon header to toggle popup sheet visible overlay state
                    IconButton(onClick = { showCartSheet = true }) {
                        BadgedBox(
                            badge = { if (totalItemsCount > 0) Badge { Text(totalItemsCount.toString()) } }
                        ) {
                            Icon(Icons.Default.ShoppingCart, contentDescription = "Open Bag Window View")
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Button(onClick = { showCartSheet = true }) {
                Text("View Shopping Cart Popup Sheet")
            }
        }

        // 3. DECLARATIVE MODAL BOTTOM SHEET OVERLAY CONTEXT
        if (showCartSheet) {
            ModalBottomSheet(
                onDismissRequest = { showCartSheet = false },
                dragHandle = { BottomSheetDefaults.DragHandle() },
                containerColor = Color.White
            ) {
                ShoppingCartSheetContent(
                    cartItems = cartListState,
                    onUpdateQuantity = { itemId, newQty ->
                        cartListState = cartListState.map { item ->
                            if (item.id == itemId) item.copy(initialQuantity = newQty) else item
                        }.filter { it.initialQuantity > 0 } // Drop item from list entirely if quantity reaches 0
                    },
                    onCheckoutClicked = {
                        showCartSheet = false
                        /* Navigate to Checkout Screen */
                    }
                )
            }
        }
    }
}

// ==========================================
// 4. BOTTOM SHEET BODY LAYOUT VIEW
// ==========================================
@SuppressLint("DefaultLocale")
@Composable
fun ShoppingCartSheetContent(
    cartItems: List<CartLineItem>,
    onUpdateQuantity: (Int, Int) -> Unit,
    onCheckoutClicked: () -> Unit
) {
    val totalCartPrice = cartItems.sumOf { it.price * it.initialQuantity }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding() // Ensures structural safety clearance around navigation bars
            .padding(horizontal = 20.dp, vertical = 8.dp)
    ) {
        Text("Your Shopping Bag", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))

        if (cartItems.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Your shopping bag is completely empty.", color = Color.Gray)
            }
        } else {
            // High-performance vertical scrolling column segment
            LazyColumn(
                modifier = Modifier.weight(1f, fill = false),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(cartItems, key = { it.id }) { item ->
                    CartProductRow(lineItem = item, onQuantityModified = { qty -> onUpdateQuantity(item.id, qty) })
                }
            }

            HorizontalDivider(color = Color(0xFFF1F1F1), modifier = Modifier.padding(vertical = 16.dp))

            // Subtotal Price Metadata Calculation Block
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Subtotal:", fontSize = 15.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
                Text(
                    text = "$${String.format("%.2f", totalCartPrice)}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Button(
                onClick = onCheckoutClicked,
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Proceed to Checkout", fontWeight = FontWeight.Bold)
            }
        }
    }
}

// ==========================================
// 5. INDIVIDUAL CART ITEM ROW VIEW
// ==========================================
@Composable
fun CartProductRow(lineItem: CartLineItem, onQuantityModified: (Int) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Thumbnail Image Placeholder box
        Box(
            modifier = Modifier
                .size(64.dp)
                .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text("📦", fontSize = 22.sp)
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Title and Base Price Info Frame
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = lineItem.title,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "$${lineItem.price}",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.Bold
            )
        }

        // Stepper Quantity Multiplier Controls Frame
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.background(Color(0xFFF5F5F5), CircleShape).padding(horizontal = 4.dp, vertical = 2.dp)
        ) {
            FilledIconButton(
                onClick = { onQuantityModified(lineItem.initialQuantity - 1) },
                colors = IconButtonDefaults.filledIconButtonColors(containerColor = Color.Transparent),
                modifier = Modifier.size(28.dp)
            ) {
                Icon(
                    imageVector = if (lineItem.initialQuantity == 1) Icons.Default.Delete else Icons.Default.Clear,
                    contentDescription = "Reduce Quantity count",
                    tint = if (lineItem.initialQuantity == 1) Color.Red else Color.Black,
                    modifier = Modifier.size(16.dp)
                )
            }

            Text(
                text = lineItem.initialQuantity.toString(),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier.padding(horizontal = 6.dp)
            )

            FilledIconButton(
                onClick = { onQuantityModified(lineItem.initialQuantity + 1) },
                colors = IconButtonDefaults.filledIconButtonColors(containerColor = Color.Transparent),
                modifier = Modifier.size(28.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Increase Quantity count", tint = Color.Black, modifier = Modifier.size(16.dp))
            }
        }
    }
}