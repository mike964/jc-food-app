package com.example.gmfastfood.extra

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.gmfastfood.vm.SharedViewModel
import kotlin.text.ifEmpty
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Simple local data class for results representation
data class SearchProduct(val id: Int, val name: String, val category: String, val price: Double)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun  SearchScreen2(viewModel: SharedViewModel)  {
    // 1. Interactive States
    var searchQuery by remember { mutableStateOf("") }
    var searchHistory by remember { mutableStateOf(listOf("Headphones", "Mechanical Keyboard", "Smartwatch")) }

    val allProducts = remember { getSearchMockDataset() }

    // Derived state filtering dynamically matching text context input
    val filteredProducts = remember(searchQuery) {
        if (searchQuery.isBlank()) emptyList()
        else allProducts.filter { it.name.contains(searchQuery, ignoreCase = true) || it.category.contains(searchQuery, ignoreCase = true) }
    }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF8F9FA))
        ) {
            // 2. Search Text Input Component Box
            SearchBarContainer(
                query = searchQuery,
                onQueryChange = { searchQuery = it },
                onClearClick = { searchQuery = "" }
            )

            // 3. Conditional Content Switcher
            if (searchQuery.isBlank()) {
                // Show Search History and Quick Tags when search input field is completely empty
                RecentSearchesSection(
                    history = searchHistory,
                    onHistoryItemClick = { searchQuery = it },
                    onClearHistory = { searchHistory = emptyList() }
                )
            } else {
                // Show filtered catalog query list
                SearchResultsSection(
                    results = filteredProducts,
                    queryText = searchQuery
                )
            }
        }
    }
}

// ==========================================
// SUB-COMPONENT VIEWS
// ==========================================

@Composable
fun SearchBarContainer(
    query: String,
    onQueryChange: (String) -> Unit,
    onClearClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        TextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clip(RoundedCornerShape(28.dp)),
            placeholder = { Text("Search electronic devices, brands...", color = Color.Gray, fontSize = 15.sp) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Symbol", tint = Color.Gray) },
            trailingIcon = {
                AnimatedVisibility(visible = query.isNotEmpty(), enter = fadeIn(), exit = fadeOut()) {
                    IconButton(onClick = onClearClick) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear text layout field")
                    }
                }
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFF1F3F4),
                unfocusedContainerColor = Color(0xFFF1F3F4),
                disabledContainerColor = Color(0xFFF1F3F4),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            singleLine = true
        )
    }
}

@Composable
fun RecentSearchesSection(
    history: List<String>,
    onHistoryItemClick: (String) -> Unit,
    onClearHistory: () -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        if (history.isNotEmpty()) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Recent Searches", fontWeight = FontWeight.Bold, color = Color.DarkGray, fontSize = 14.sp)
                Text(
                    text = "Clear All",
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 13.sp,
                    modifier = Modifier.clickable { onClearHistory() }
                )
            }

            // Loop historical options seamlessly
            history.forEach { historyTerm ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onHistoryItemClick(historyTerm) }
                        .padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = null, tint = Color.LightGray, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(historyTerm, fontSize = 15.sp, color = Color.Black)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Popular Categories", fontWeight = FontWeight.Bold, color = Color.DarkGray, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(12.dp))

        // Suggestion Chip Row
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("Audio", "Phones", "Laptops").forEach { tag ->
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color(0xFFE8EAED))
                        .clickable { onHistoryItemClick(tag) }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(tag, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}

@Composable
fun SearchResultsSection(results: List<SearchProduct>, queryText: String) {
    if (results.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("🕵️", fontSize = 48.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text("No matching results for \"$queryText\"", color = Color.Gray, fontSize = 15.sp)
            }
        }
    } else {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text("Results (${results.size})", fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 4.dp))
            }
            items(results) { product ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(product.name, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                            Text(product.category, color = Color.Gray, fontSize = 12.sp)
                        }
                        Text(
                            text = "$${product.price}",
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

fun getSearchMockDataset(): List<SearchProduct> = listOf(
    SearchProduct(1, "Wireless ANC Headphones", "Audio", 129.99),
    SearchProduct(2, "Studio Noise-Cancelling Earbuds", "Audio", 89.99),
    SearchProduct(3, "Flagship Phone 5G", "Smartphones", 899.99),
    SearchProduct(4, "Pro Mechanical Keyboard", "Accessories", 84.99),
    SearchProduct(5, "Ultra Thin 14\" Laptop", "Laptops", 1049.00)
)

@Composable
fun SearchScreen(viewModel: SharedViewModel) {
    val text by viewModel.sharedText.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Tab 2: Retrieved Data", style = MaterialTheme.typography.titleMedium)
//        Spacer(modifier = Modifier.height(16.png))
        Text(
            text = text.ifEmpty { "No data entered yet." },
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary
        )
    }
}