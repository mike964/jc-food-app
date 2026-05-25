package com.example.gmfastfood

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.gmfastfood.vm.SharedViewModel

@Composable
fun SearchPopupScreen(
    isOpen: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    viewModel : SharedViewModel
) {
    // If state is false, do not render anything
    if (!isOpen) return

    // Collect states safely from the ViewModel
    val searchQuery by viewModel.searchQuery.collectAsState()
    val books by viewModel.filteredBooks.collectAsState()

    Dialog(
        // Handles hardware back button clicks and clicking outside the popup card
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false // Required to override default width
        )
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.92f) // Now this modifier will be respected
                .wrapContentHeight(),
           shape = RoundedCornerShape(20.dp),
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                SearchBox(
                    textValue = searchQuery,
                    onValueChange = { viewModel.onSearchQueryChanged(it) }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Results List
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 400.dp) // Limit height to prevent overflow in dialog
                ) {
                    items(books, key = { it.id }) { book ->
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(text = book.title, style = MaterialTheme.typography.titleMedium)
                                Text(text = "By ${book.author}", style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Subtitle/Body description
                Text(
                    text = "If you head back now, you will lose all the progress made on this draft.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Action Layout
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // 1. The Cancel Button (Outlined/Text styled for secondary weight)
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancel")
                    }

                    // 2. The Primary Action Button
                    Button(
                        onClick = {
                            onConfirm()
                            onDismiss() // Safely close layout stack
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        ),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Discard")
                    }
                }
            }
        }
    }
}