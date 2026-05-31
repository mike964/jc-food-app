package com.example.gmfastfood.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
//import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import java.util.UUID

data class SavedAddress(
    val id: String = UUID.randomUUID().toString(),
    val label: String = "",           // e.g., "Home", "Work", "Partner's Place"
    val streetAddress: String = "",   // e.g., "123 Main Street, Apt 4B"
    val apartmentSuite: String = "",  // e.g., "Floor 4, Suite 12"
    val deliveryNotes: String = "",   // e.g., "Ring bell twice, leave at reception"
    val isDefault: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressEditScreen(
    initialAddress: SavedAddress?, // Null means we are creating a brand new address
    onBackClick: () -> Unit,
    onSaveClick: (SavedAddress) -> Unit,
    onDeleteClick: ((String) -> Unit)? = null // Null hides delete button (e.g., when creating new)
) {
    // 1. Maintain local states for form fields
    var label by remember { mutableStateOf(initialAddress?.label ?: "") }
    var streetAddress by remember { mutableStateOf(initialAddress?.streetAddress ?: "") }
    var apartmentSuite by remember { mutableStateOf(initialAddress?.apartmentSuite ?: "") }
    var deliveryNotes by remember { mutableStateOf(initialAddress?.deliveryNotes ?: "") }
    var isDefault by remember { mutableStateOf(initialAddress?.isDefault ?: false) }

    // 2. Form Error/Validation states
    var streetAddressError by remember { mutableStateOf<String?>(null) }
    var labelError by remember { mutableStateOf<String?>(null) }

    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (initialAddress == null) "Add Address" else "Edit Address",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    // Show delete icon if modifying an existing record
                    if (initialAddress != null && onDeleteClick != null) {
                        IconButton(onClick = { onDeleteClick(initialAddress.id) }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete Address",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(4.dp))

            // 3. Address Label Selector Chips
            Text(
                text = "Address Label",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val presets = listOf("Home" to Icons.Default.Home, "Work" to Icons.Default.Email, "Other" to Icons.Default.LocationOn)
                presets.forEach { (presetName, icon) ->
                    FilterChip(
                        selected = label.equals(presetName, ignoreCase = true),
                        onClick = {
                            label = presetName
                            labelError = null
                        },
                        label = { Text(presetName) },
                        leadingIcon = { Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(18.dp)) }
                    )
                }
            }

            // Custom dynamic label input if "Other" or custom is desired
            OutlinedTextField(
                value = label,
                onValueChange = {
                    label = it
                    if (it.isNotBlank()) labelError = null
                },
                label = { Text("Custom Label (Optional)") },
                isError = labelError != null,
                supportingText = labelError?.let { { Text(it) } },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Next
                )
            )

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            // 4. Main Street Address Input
            OutlinedTextField(
                value = streetAddress,
                onValueChange = {
                    streetAddress = it
                    if (it.isNotBlank()) streetAddressError = null
                },
                label = { Text("Street Address") },
                placeholder = { Text("e.g., 1024 Broadway Ave") },
                isError = streetAddressError != null,
                supportingText = streetAddressError?.let { { Text(it) } },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Next
                )
            )

            // 5. Apartment / Suite / Floor (Optional)
            OutlinedTextField(
                value = apartmentSuite,
                onValueChange = { apartmentSuite = it },
                label = { Text("Apt / Suite / Floor (Optional)") },
                placeholder = { Text("e.g., Apt 4B, 3rd Floor") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Next
                )
            )

            // 6. Delivery Notes Text Area
            OutlinedTextField(
                value = deliveryNotes,
                onValueChange = { deliveryNotes = it },
                label = { Text("Delivery Instructions") },
                placeholder = { Text("e.g., Drop off at security gate, call on arrival...") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 5,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Done
                )
            )

            // 7. Toggle switch to set as favorite / primary address
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Set as default address",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "Use this address as your default option for checkouts.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Switch(
                    checked = isDefault,
                    onCheckedChange = { isDefault = it }
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // 8. Bottom Action Save Button
            Button(
                onClick = {
                    // Quick validation scan execution
                    var hasError = false
                    if (streetAddress.isBlank()) {
                        streetAddressError = "Street address cannot be empty"
                        hasError = true
                    }
                    if (label.isBlank()) {
                        labelError = "Please select or type an address label"
                        hasError = true
                    }

                    if (!hasError) {
                        val finalAddress = SavedAddress(
                            id = initialAddress?.id ?: UUID.randomUUID().toString(),
                            label = label.trim(),
                            streetAddress = streetAddress.trim(),
                            apartmentSuite = apartmentSuite.trim(),
                            deliveryNotes = deliveryNotes.trim(),
                            isDefault = isDefault
                        )
                        onSaveClick(finalAddress)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    text = if (initialAddress == null) "Save Address" else "Update Address",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}