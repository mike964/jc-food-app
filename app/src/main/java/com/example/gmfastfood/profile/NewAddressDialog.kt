package com.example.gmfastfood.profile


import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.ui.Modifier


@Composable
fun NewAddressDialog(   onDismiss: () -> Unit){

    var noteText = ""

    // The Popup / Dialog
        AlertDialog(
            onDismissRequest = {
//                showAddAddressDialog = false
                noteText = "" // Optional: clear text on dismiss
            },
            title = { Text("New Note") },
            text = {
                Column {
                    OutlinedTextField(
                        value = noteText,
                        onValueChange = { noteText = it },
                        placeholder = { Text("Enter note details...") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        // TODO: Save your note to the database or viewmodel here
//                        showAddAddressDialog = false
                        noteText = ""
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