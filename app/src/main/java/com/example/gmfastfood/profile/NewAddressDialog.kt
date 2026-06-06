package com.example.gmfastfood.profile


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.gmfastfood.map.LeafletMap
import org.intellij.lang.annotations.JdkConstants


@Composable
fun NewAddressDialog(
    onDismiss: () -> Unit,
    onSaveClick: () -> Unit,
    newAddressLabel: String,
    newAddressText: String,
    newAddressNote: String,
    onAddressChange: (String) -> Unit,
    onLabelChange: (String) -> Unit,
    onSelectedLocation: (Double, Double) -> Unit,
    // isEditing: Boolean = false
) {
    // The Popup / Dialog
    Dialog(
        onDismissRequest = {
//                showAddAddressDialog = false
            onDismiss()
        }
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
//                .padding(4.dp),
            , shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("New Address",
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth())
                OutlinedTextField(
                    value = newAddressLabel,
                    onValueChange = onLabelChange,
                    placeholder = { Text("Label...") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = newAddressText,
                    onValueChange = onAddressChange,
                    placeholder = { Text("Full address...") },
                    modifier = Modifier.fillMaxWidth()
                )

                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)) {
                    LeafletMap(
                        startingLocation = "",
                        onLocationSelected = { lat, lng ->
                            onSelectedLocation(lat, lng)
                        })
                }

                Row {
                    Button(
                        onClick = {
                            // TODO: Save your note to the database or viewmodel here
                            onSaveClick()
                            onDismiss()
                        }
                    ) {
                        Text("Save")
                    }
                    TextButton(
                        onClick = {
                            onDismiss()
                        }
                    ) {
                        Text("Cancel")
                    }
                }
            }
        }
    }
}