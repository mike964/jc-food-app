package com.example.gmfastfood

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.gmfastfood.data.Product
import com.example.gmfastfood.vm.CartItem
import com.example.gmfastfood.vm.CartViewModel
import com.example.gmfastfood.vm.SharedViewModel

@Composable
fun SearchPopupScreen(
    isOpen: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    viewModel: SharedViewModel,
    cartViewModel: CartViewModel,
) {
    // If state is false, do not render anything
    if (!isOpen) return

    // Collect states safely from the ViewModel
    val searchQuery by viewModel.searchQuery.collectAsState()
    val books by viewModel.filteredBooks.collectAsState()
    val products by viewModel.filteredProducts.collectAsState()

    Dialog(
        // Handles hardware back button clicks and clicking outside the popup card
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false // Required to override default width
        )
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.92f) // Now this modifier will be respected
                .wrapContentHeight(),
           shape = RoundedCornerShape(20.dp),
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
//                    .height(600.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                SearchBox(
                    textValue = searchQuery,
                    onValueChange = { viewModel.onSearchQueryChanged(it) }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Results List
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 400.dp, min = 400.dp) // Limit height to prevent overflow in dialog
                ) {
                    items(products, key = { it.id }) { item ->
//                        Card(modifier = Modifier.fillMaxWidth()) {
//                            Column(modifier = Modifier.padding(16.dp)) {
//                                Text(text = book.title, style = MaterialTheme.typography.titleMedium)
////                                Text(text = "By ${book.author}", style = MaterialTheme.typography.bodyMedium)
//                            }
//                        }
                        ProductItemRow(item = item,
                            addToCart = { cartViewModel.addToCart(CartItem(item.id, imageUrl = item.image.toString(), name = item.title, price = item.price, quantity = 1)) },
                            onQuantityModified = {
                              //  qty -> onUpdateQuantity(item.id, qty)
                            }
                        )

                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Action Layout
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // 1. The Cancel Button (Outlined/Text styled for secondary weight)
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancel")
                    }

                    // 2. The Primary Action Button
                    Button(
                        onClick = {
                            onConfirm()
                            onDismiss() // Safely close layout stack
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        ),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Discard")
                    }
                }
            }
        }
    }
}


// ==========================================
// 5. INDIVIDUAL CART ITEM ROW VIEW
// ==========================================
@SuppressLint("DefaultLocale")
@Composable
fun ProductItemRow(item: Product,  addToCart : () -> Unit , onQuantityModified: (Int) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Thumbnail Image Placeholder box
        Box(
            modifier = Modifier
                .size(64.dp)
                .background(Color(0xFFF5F5F5),
                    RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Image(
           //     painter = painterResource(item.imageUrl.toIntOrNull() ?: R.drawable.burger),
                painter = painterResource(item.image  ),
                contentDescription = "item image",
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .fillMaxSize(), // Makes the Composable fill parent bounds
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Title and Base Price Info Frame
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.title,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = String.format("%.0f", item.price),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.Bold
            )
        }

        // Stepper Quantity Multiplier Controls Frame
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .background(Color(0xFFF5F5F5), CircleShape)
                .padding(horizontal = 4.dp, vertical = 2.dp)
        ) {

            FilledIconButton(
                onClick = {  addToCart() },
                colors = IconButtonDefaults.filledIconButtonColors(containerColor = Color.Transparent),
                modifier = Modifier.size(28.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Increase Quantity count", tint = Color.Black, modifier = Modifier.size(16.dp))
            }
        }
    }
}