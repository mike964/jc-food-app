package com.example.gmfastfood.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gmfastfood.data.ApiItem
import com.example.gmfastfood.data.FakeApiClient
import com.example.gmfastfood.data.FakeStoreApiClient
import com.example.gmfastfood.data.Product
import com.example.gmfastfood.data.StoreApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface  UiState {
    object Loading :  UiState
    data class Success(val items: List<Product>) :  UiState
    data class Error(val message: String) :  UiState
}

class SharedViewModel(
    private val apiService: FakeApiClient = FakeApiClient()
) : ViewModel() {
    private val _sharedText = MutableStateFlow("")
    val sharedText: StateFlow<String> = _sharedText.asStateFlow()

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        loadDataFromNetwork()
    }

    fun loadDataFromNetwork() {
        viewModelScope.launch {
            _uiState.update { UiState.Loading }
            try {
                val data = apiService.getProductItems()
                _uiState.update { UiState.Success(items = data) }
            } catch (e: Exception) {
                _uiState.update { UiState.Error(message = e.localizedMessage ?: "Unknown Error") }
            }
        }
    }

    fun updateText(newText: String) {
        _sharedText.value = newText
    }
}