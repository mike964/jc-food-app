package com.example.gmfastfood.data

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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

// ==========================================
// 1. DATA MODEL
// ==========================================
data class ApiItem(val id: Int, val title: String, val body: String)

// ==========================================
// 2. THE API INTERFACE & MOCK IMPLEMENTATION
// ==========================================
interface StoreApiService {
    suspend fun getDashboardItems(): List<ApiItem>
}

class FakeStoreApiClient : StoreApiService {
    private var toggleErrorFlag = false // Flag to simulate random network dropouts

    override suspend fun getDashboardItems(): List<ApiItem> {
        // 1. Simulate network latency (2.5 seconds delay)
        delay(2500)

        // 2. Intermittent error injection to test UI resilience
        toggleErrorFlag = !toggleErrorFlag
        if (toggleErrorFlag) {
            throw IOException("Unable to reach store server. Please check your network connection.")
        }

        // 3. Mock database payload data returned on success paths
        return listOf(
            ApiItem(1, "🎉 Welcome Offer", "Get 20% off your first checkout using code FIRST20."),
            ApiItem(2, "🔥 Flash Sale Ending", "Smartwatches are 15% off for the next two hours."),
            ApiItem(3, "📦 Restock Alert", "Mechanical Keyboards are back in stock and ready to ship.")
        )
    }
}

// ==========================================
// 3. THE UI STATE ARCHETYPE
// ==========================================
sealed interface DashboardUiState {
    object Loading : DashboardUiState
    data class Success(val items: List<ApiItem>) : DashboardUiState
    data class Error(val message: String) : DashboardUiState
}

// ==========================================
// 4. THE VIEWMODEL (Asynchronous State Machine)
// ==========================================
class DashboardViewModel(
    private val apiService: StoreApiService = FakeStoreApiClient()
) : ViewModel() {

    private val _uiState = MutableStateFlow<DashboardUiState>(DashboardUiState.Loading)
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        loadDataFromNetwork()
    }

    fun loadDataFromNetwork() {
        viewModelScope.launch {
            _uiState.update { DashboardUiState.Loading }
            try {
                val data = apiService.getDashboardItems()
                _uiState.update { DashboardUiState.Success(items = data) }
            } catch (e: Exception) {
                _uiState.update { DashboardUiState.Error(message = e.localizedMessage ?: "Unknown Error") }
            }
        }
    }
}

// ==========================================
// 5. VIEW LAYERS (Jetpack Compose Layouts)
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(dashboardViewModel: DashboardViewModel = viewModel()) {
    val state by dashboardViewModel.uiState.collectAsState()

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
                is DashboardUiState.Loading -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(strokeWidth = 4.dp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Simulating fetch request (2.5s)...", color = Color.Gray, fontSize = 14.sp)
                    }
                }
                is DashboardUiState.Success -> {
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
                                    Text(item.body, fontSize = 13.sp, color = Color.DarkGray, lineHeight = 18.sp)
                                }
                            }
                        }
                    }
                }
                is DashboardUiState.Error -> {
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
                        Button(onClick = { dashboardViewModel.loadDataFromNetwork() }) {
                            Text("Retry Network Fetch Call")
                        }
                    }
                }

            }
        }
    }
}