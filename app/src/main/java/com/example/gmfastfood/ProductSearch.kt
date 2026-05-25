package com.example.gmfastfood

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gmfastfood.vm.SharedViewModel

@Composable
fun BookSearchScreen(viewModel: SharedViewModel = viewModel()) {
    // Collect states safely from the ViewModel
    val searchQuery by viewModel.searchQuery.collectAsState()
    val books by viewModel.filteredBooks.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { viewModel.onSearchQueryChanged(it) },
            label = { Text("Search books or authors...") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Results List
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
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
    }
}
