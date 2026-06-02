package com.example.gmfastfood.profile


import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun NewAddressDialog(
    onDismiss: () -> Unit,
    onSaveClick: () -> Unit,
    newAddressLabel: String,
    newAddressText: String,
    newAddressNote: String,
    onAddressChange: (String) -> Unit,
    onLabelChange: (String) -> Unit,
   // isEditing: Boolean = false
) {


    // The Popup / Dialog
    AlertDialog(
        onDismissRequest = {
//                showAddAddressDialog = false
            onDismiss()
        },
        title = { Text("New Address") },
        text = {
            Column {
                OutlinedTextField(
                    value = newAddressLabel,
                    onValueChange = onLabelChange,
                    placeholder = { Text("Label...") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = newAddressText,
                    onValueChange = onAddressChange,
                    placeholder = { Text("Full address...") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    // TODO: Save your note to the database or viewmodel here
                    onSaveClick()
                    onDismiss()
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismiss()
                }
            ) {
                Text("Cancel")
            }
        }
    )
}