package com.example.gmfastfood.checkout

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
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
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gmfastfood.data.Order
import com.example.gmfastfood.data.UserAddress
import com.example.gmfastfood.data.generateRandomId
import com.example.gmfastfood.profile.NewAddressDialog
import com.example.gmfastfood.vm.CartViewModel
import com.example.gmfastfood.vm.SharedViewModel

@SuppressLint("DefaultLocale")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    onBackClicked: () -> Unit,
    navigateToOrderDetails: (String) -> Unit,
    viewModel: SharedViewModel,
    cartViewModel: CartViewModel,
) {
//    val options = listOf("Option 1", "Option 2", "Option 3")
    val cartItems = cartViewModel.uiState.collectAsState().value.cartItems
    val cartTotal = cartItems.sumOf { it.price * it.quantity }
    val cartTotalFormatted = String.format("%.2f", cartTotal)
    val shipping = 2000.0

    var addresses by remember { mutableStateOf(viewModel.addresses.value) }
    val addressOptions = addresses.map { it.fullAddress }
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(addressOptions[0]) }

    var showAddAddressDialog by remember { mutableStateOf(false) }
    var newAddressText by remember { mutableStateOf("") }
    var newAddressNote by remember { mutableStateOf("") }
    var newAddressLabel by remember { mutableStateOf("") }
    var selectedPointLat by remember { mutableDoubleStateOf(0.0) }
    var selectedPointLng by remember { mutableDoubleStateOf(0.0) }

    // 1. Define the options
    val radioOptions = listOf("Option A", "Option B", "Option C", "Option D")
    val paymentOptions = listOf( "Cash on Delivery", "Credit Card", "PayPal")

    @Composable
    fun paymentOptionIcon(option: String){
        var icon: String = ""
        when(option){
            "Cash on Delivery" ->  icon = "🛵"
            "Credit Card" -> icon = "💳"
            "PayPal" ->  icon = "💸"
        }
        return Text(icon,fontSize = 20.sp, modifier = Modifier.padding(horizontal = 8.dp))
    }

    // 2. Track the selected option state
    val (selectedPaymentOption, onPaymentOptionSelected) = remember { mutableStateOf(radioOptions[0]) }



    fun onSaveNewAddress() {
        val newAddress = UserAddress(
            id = generateRandomId(8),
            label = newAddressLabel,
            fullAddress = newAddressText,
            note = newAddressNote,
            isDefault = false,
            latitude = selectedPointLat,
            longitude = selectedPointLng
        )
        viewModel.addAddress(newAddress)
        addresses = viewModel.addresses.value
        expanded = false
        selectedOption = newAddressText
        showAddAddressDialog = false
        // Clear text fields after dismiss
        newAddressText = ""
        newAddressNote = ""
        newAddressLabel = ""
        selectedPointLat = 0.0
        selectedPointLng = 0.0
    }

    fun handleSubmitOrder() {
        val newOrderId = generateRandomId(8)

        val order = Order(
            id = newOrderId,
            createdAt = System.currentTimeMillis(),
            status = "In Transit",
            items = cartItems,
            subtotal = cartTotal,
            shipping = 0.0,
            tax = 0.0,
            total = cartTotal,
            address = selectedOption,
            note = ""
        )
        viewModel.addOrderToSubmit(order)
        cartViewModel.clearCart()
        navigateToOrderDetails(newOrderId)  // Navigate to Order details screen
    }

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
        if (showAddAddressDialog) {
            NewAddressDialog(
                onSaveClick = {
                    onSaveNewAddress()
                },
                onDismiss = {
                    showAddAddressDialog = false
                    // Clear text fields after dismiss
                    newAddressText = ""
                    newAddressNote = ""
                    newAddressLabel = ""
                },
                newAddressLabel = newAddressLabel,
                newAddressText = newAddressText,
                newAddressNote = newAddressNote,
                onAddressChange = { newAddressText = it },
                onLabelChange = { newAddressLabel = it },
//                onNoteChange = { newAddressNote = it }
                onSelectedLocation = { lat, lng ->
                    selectedPointLat = lat
                    selectedPointLng = lng
                }
            )
        }
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

                // 1. Shipping Section
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Shipping Address", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                        TextButton(onClick = {
                            showAddAddressDialog = true
                        }) {
                            Text("New Address", fontSize = 13.sp)
                        }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
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
                                label = { Text("Select an address") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                            )

                            if (addresses.isNotEmpty()) {
                                ExposedDropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false }
                                ) {
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
                        }
                    }
                    Text(
                        selectedOption,
                        Modifier.padding(24.dp, 8.dp),
                        color = Color.Gray,
                        fontSize = 13.sp
                    )
                }

                // 2. Payment Section
                CheckoutSectionCard(
                    title = "Payment Method",
                    icon = Icons.Default.CheckCircle,
                    actionText = "Edit"
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(
                            // The selectableGroup modifier is great for accessibility (screen readers)
                            modifier = Modifier.selectableGroup().padding(4.dp)
                        ) {
//                            Text(
//                                "Mastercard ending in 4321",
//                                fontWeight = FontWeight.SemiBold,
//                                fontSize = 14.sp
//                            )
//                            Text("Expires 12/29", color = Color.Gray, fontSize = 12.sp)

                            paymentOptions.forEach { payOption ->
                                Row(
                                    Modifier
                                        .fillMaxWidth()
                                        // Make the entire row clickable for better UX
                                        .selectable(
                                            selected = (payOption == selectedPaymentOption),
                                            onClick = { onPaymentOptionSelected(payOption) },
                                            role = Role.RadioButton
                                        )
                                        .padding(vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    RadioButton(
                                        selected = (payOption == selectedPaymentOption),
                                        onClick = null // null because the Row's selectable handles the click
                                    )
                                    paymentOptionIcon(payOption)
                                    Text(
                                        text = payOption,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 14.sp,
                                        modifier = Modifier.padding(horizontal = 8.dp)
                                    )
                                }
                            }
                        }
                    }
                }
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
                        Text("Subtotal:", fontSize = 14.sp, color = Color.Gray)
                        Text(
                            text = String.format("%.0f", cartTotal),
//                            fontSize = 22.sp,
//                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Shipping")
                        Text(if (shipping == 0.0) "FREE" else String.format("%.0f", shipping))
                    }
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        thickness = DividerDefaults.Thickness,
                        color = DividerDefaults.color
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Total",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            "IQD ${String.format("%.0f", cartTotal + shipping)}",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Button(
                        onClick = { handleSubmitOrder() },
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

//                    AnimatedSubmitButton(
//                        isLoading = false,
//                        onClick = { handleSubmitOrder() }
//                    )
                }
            }
        }
    }
}

