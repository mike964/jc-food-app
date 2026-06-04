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
import com.example.gmfastfood.vm.SharedViewModel
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressListScreen(
    selectedAddressId: String?,
    onBackClick: () -> Unit,
    onAddressSelect: (SavedAddress) -> Unit,
    onEditClick: (SavedAddress) -> Unit,
    onAddNewAddressClick: () -> Unit,
    viewModel: SharedViewModel,
) {
    val addresses = viewModel.addresses.collectAsState().value
    var showAddAddressDialog by remember { mutableStateOf(false) }
    var newAddressText by remember { mutableStateOf("") }
    var newAddressNote by remember { mutableStateOf("") }
    var newAddressLabel by remember { mutableStateOf("") }
//    var selectedAddress by remember { mutableStateOf<SavedAddress?>(null) }


//    LaunchedEffect(selectedAddressId) {
//        selectedAddress = addresses.find { it.id == selectedAddressId }
//    }

    fun onSaveNewAddressClick() {
        if (newAddressText.isNotBlank()) {
            val newAddress = UserAddress(
                id = UUID.randomUUID().toString(),
                label = newAddressLabel,
                fullAddress = newAddressText,
                note = newAddressNote,
                isDefault = false
            )
            viewModel.addAddress(newAddress)
        }
    }

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
                onClick = { showAddAddressDialog = true },
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("Add New") },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(16.dp)
            )
        }
    ) { paddingValues ->
        if (showAddAddressDialog) {
            NewAddressDialog(
                onSaveClick = { onSaveNewAddressClick() },
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
            )
        }
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
