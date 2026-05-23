package com.example.gmfastfood.screens
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gmfastfood.vm.SharedViewModel
import com.example.gmfastfood.vm.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestScreen(viewModel: SharedViewModel) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Network Sync Engine", fontWeight = FontWeight.Bold) }) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF8F9FA)),
            contentAlignment = Alignment.Center
        ) {
            // Decoupled state layout processing switcher
            when (val currentState = state) {
                is  UiState.Loading -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(strokeWidth = 4.dp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Simulating fetch request (2.5s)...", color = Color.Gray, fontSize = 14.sp)
                    }
                }
                is  UiState.Success -> {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        item { Text("Latest Announcements", fontWeight = FontWeight.Bold, fontSize = 18.sp) }
                        items(currentState.items) { item ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(item.title, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = MaterialTheme.colorScheme.primary)
                                    Spacer(modifier = Modifier.height(4.dp))
//                                    Text(item.body, fontSize = 13.sp, color = Color.DarkGray, lineHeight = 18.sp)
                                }
                            }
                        }
                    }
                }
                is UiState.Error -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(32.dp)
                    ) {
                        Text("⚠️", fontSize = 42.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Network Failure", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(currentState.message, color = Color.Gray, fontSize = 13.sp, modifier = Modifier.padding(horizontal = 16.dp))
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.loadDataFromNetwork() }) {
                            Text("Retry Network Fetch Call")
                        }
                    }
                }
            }
        }
    }
}