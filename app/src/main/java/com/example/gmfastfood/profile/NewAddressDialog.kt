package com.example.gmfastfood.profile


import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier


@Composable
fun NewAddressDialog(
    onDismiss: () -> Unit,
    onSaveClick: () -> Unit,
    newAddressLabel: String,
    newAddressText: String,
    newAddressNote: String,
    onAddressChange: (String) -> Unit,
) {

    var noteText = ""

    // The Popup / Dialog
    AlertDialog(
        onDismissRequest = {
//                showAddAddressDialog = false
            noteText = "" // Optional: clear text on dismiss
        },
        title = { Text("New Address") },
        text = {
            Column {
                OutlinedTextField(
                    value = newAddressText,
                    onValueChange = onAddressChange,
                    placeholder = { Text("Enter full address...") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    // TODO: Save your note to the database or viewmodel here
                    onSaveClick()
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
//                        showAddAddressDialog = false
                    noteText = ""
                    onDismiss()
                }
            ) {
                Text("Cancel")
            }
        }
    )
}