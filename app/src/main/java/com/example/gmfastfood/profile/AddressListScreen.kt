package com.example.gmfastfood.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.gmfastfood.data.UserAddress

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressListScreen(

    selectedAddressId: String?,
    onBackClick: () -> Unit,
    onAddressSelect: (SavedAddress) -> Unit,
    onEditClick: (SavedAddress) -> Unit,
    onAddNewAddressClick: () -> Unit,
) {
    val addresses = listOf(
        UserAddress(
            id = "1",
            label = "Home",
            fullAddress = "123 Main St, Apt 4B",
            deliveryNotes = "Ring bell twice, leave at reception",
            isDefault = true
        ),
        UserAddress(
            id = "2",
            label = "Work",
            fullAddress = "324 Main St, Apt 2C",
            deliveryNotes = "No need for extra stuff",
            isDefault = false
        )
    )
    var showAddAddressDialog by remember { mutableStateOf(false) }
    var selectedAddress by remember { mutableStateOf<SavedAddress?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Saved Addresses", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onAddNewAddressClick,
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("Add New") },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(16.dp)
            )
        }
    ) { paddingValues ->
        if (addresses.isEmpty()) {
            EmptyAddressState(
                modifier = Modifier.padding(paddingValues),
                onAddClick = onAddNewAddressClick
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(addresses, key = { it.id }) { address ->
                    AddressRowItem(
                        address = address,
                        isSelected = address.id == selectedAddressId,
                        onSelect = {
                            //   onAddressSelect(address)
                        },
                        onEdit = {
                            //     onEditClick(address)
                        }
                    )
                }
            }
        }
    }
}
