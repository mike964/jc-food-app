package com.example.gmfastfood.checkout

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gmfastfood.profile.AddressRowItem
import com.example.gmfastfood.vm.CartViewModel
import com.example.gmfastfood.vm.SharedViewModel

@SuppressLint("DefaultLocale")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    onBackClicked: () -> Unit = {},
    onOrderPlaced: () -> Unit = {},
    viewModel: SharedViewModel,
    cartViewModel: CartViewModel,
) {
//    val options = listOf("Option 1", "Option 2", "Option 3")
    val cartItems = cartViewModel.uiState.collectAsState().value.cartItems
    val cartTotal = cartItems.sumOf { it.price * it.quantity }
    val cartTotalFormatted = String.format("%.2f", cartTotal)

    var addresses by remember { mutableStateOf(viewModel.addresses.value) }
    val addressOptions = addresses.map { it.fullAddress }
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(addressOptions[0]) }



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
                .background(Color(0xFFF8F9FA))
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

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        OutlinedTextField(
                            // The menuAnchor modifier links the TextField to the dropdown behavior
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth(),
                            readOnly = true,
                            value = selectedOption,
                            onValueChange = {},
                            label = { Text("Select an option") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                        )

                        if (addresses.isNotEmpty()) {
                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
//                            options.forEach { selectionOption ->
//                                DropdownMenuItem(
//                                    text = { Text(selectionOption) },
//                                    onClick = {
//                                        selectedOption = selectionOption
//                                        expanded = false
//                                    },
//                                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
//                                )
//                            }
                                addresses.forEach { selectionOption ->
                                    DropdownMenuItem(
                                        text = { Text(selectionOption.fullAddress) },
                                        onClick = {
                                            selectedOption = selectionOption.fullAddress
                                            expanded = false
                                        },
                                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                                    )
                                }
                            }
                        }

//                        if (addresses.isNotEmpty()) {
//                            Column(
//                                modifier = Modifier
//                                    .fillMaxSize()
//                                    .padding(paddingValues),
//                                verticalArrangement = Arrangement.spacedBy(12.dp)
//                            ) {
//                                addresses.forEach {
//                                    Text(
//                                        text = it.fullAddress,
//                                    )
//                                }
//                            }
//                        }

                    }
                }

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
                            Text(
                                "Mastercard ending in 4321",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp
                            )
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
                        Text(
                            "$1,279.23",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Button(
                        onClick = onOrderPlaced,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            Icons.Default.Lock,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
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
    content: @Composable ColumnScope.() -> Unit,
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
                    Icon(
                        icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
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
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text("Order Summary", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            HorizontalDivider(color = Color(0xFFF1F1F1))

            SummaryRow(label = "Subtotal", value = "$$subtotal")
            SummaryRow(
                label = "Shipping",
                value = if (shipping == 0.0) "FREE" else "$$shipping",
                valueColor = if (shipping == 0.0) Color(0xFF2E7D32) else Color.Black
            )
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