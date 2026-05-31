package com.example.gmfastfood.cart

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gmfastfood.R
import com.example.gmfastfood.vm.CartItem

@SuppressLint("DefaultLocale")
@Composable
fun ShoppingCartContent(
    cartItems: List<CartItem>,
    onUpdateQuantity: (Int, Int) -> Unit,
    onCheckoutClicked: () -> Unit
) {
//    val totalCartPrice = cartItems.sumOf { it.price * it.initialQuantity }
    val totalCartPrice = cartItems.sumOf { it.price }

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
                    CartItemRow(item = item, onQuantityModified = { qty -> onUpdateQuantity(item.id, qty) })
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
                    //text = "$${String.format("%.2f", totalCartPrice)}",
                    text = "${String.format("%.0f", totalCartPrice)} IQD",
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
fun CartItemRow(item: CartItem, onQuantityModified: (Int) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Thumbnail Image Placeholder box
        Box(
            modifier = Modifier
                .size(64.dp)
                .background(Color(0xFFF5F5F5), RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(item.imageUrl.toIntOrNull() ?: R.drawable.burger),
                contentDescription = "item image",
                modifier = Modifier
                       .fillMaxSize(), // Makes the Composable fill parent bounds
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Title and Base Price Info Frame
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.name,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "$${item.price}",
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
                onClick = { onQuantityModified(item.quantity - 1) },
                colors = IconButtonDefaults.filledIconButtonColors(containerColor = Color.Transparent),
                modifier = Modifier.size(28.dp)
            ) {
                Icon(
                    imageVector = if (item.quantity == 1) Icons.Default.Delete else Icons.Default.Clear,
                    contentDescription = "Reduce Quantity count",
                    tint = if (item.quantity == 1) Color.Red else Color.Black,
                    modifier = Modifier.size(16.dp)
                )
            }

            Text(
                text = item.quantity.toString(),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier.padding(horizontal = 6.dp)
            )

            FilledIconButton(
                onClick = { onQuantityModified(item.quantity + 1) },
                colors = IconButtonDefaults.filledIconButtonColors(containerColor = Color.Transparent),
                modifier = Modifier.size(28.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Increase Quantity count", tint = Color.Black, modifier = Modifier.size(16.dp))
            }
        }
    }
}