package com.example.gmfastfood.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gmfastfood.data.FakeApiClient
import com.example.gmfastfood.data.Order
import com.example.gmfastfood.data.Product
import com.example.gmfastfood.data.UserAddress
import com.example.gmfastfood.data.sampleAddresses
import com.example.gmfastfood.data.sampleOrders2
import com.example.gmfastfood.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


// Define possible UI states
sealed interface UiState {
    object Loading : UiState
    data class Success(val items: List<Product>) : UiState
    data class Error(val message: String) : UiState
}

class SharedViewModel(
    private val apiService: FakeApiClient = FakeApiClient(),
) : ViewModel() {
    private val _sharedText = MutableStateFlow("")
    val sharedText: StateFlow<String> = _sharedText.asStateFlow()

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _products = MutableStateFlow<List<Product>>(emptyList())

    private var _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders = _orders.asStateFlow()

    private val _addresses = MutableStateFlow<List<UserAddress>>(emptyList())
    val addresses = _addresses.asStateFlow()

    // Injecting manually for this example. Use Hilt in production!
    private val repository = ProductRepository(
//        RetrofitClient.apiService
        apiService
    )

    init {
        // # Load data from network
        getProducts()
//        loadDataFromNetwork()
        loadInitialAddresses()
        _orders.value = sampleOrders2
    }

    fun getProducts(){
        viewModelScope.launch {
            _uiState.update { UiState.Loading }
            try {
                val data = repository.getProducts()
                _uiState.update {
                    UiState.Success(items = data)
                }
                _products.value = data
            } catch (e: Exception) {
                _uiState.update { UiState.Error(message = e.localizedMessage ?: "Unknown Error") }
            }
        }
    }

    fun loadInitialAddresses() {
        viewModelScope.launch {
//            _addresses.value = apiService.getUserAddresses()
            _addresses.value = sampleAddresses
        }
    }

    fun updateText(newText: String) {
        _sharedText.value = newText
    }

    // 1. The search query state
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    val filteredProducts = combine(_products, _searchQuery) { products, query ->
        if (query.isBlank()) {
            products
        } else {
            products.filter { product ->
                product.title.contains(query, ignoreCase = true)
//                        ||  product.author.contains(query, ignoreCase = true)
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun onSearchQueryChanged(newQuery: String) {
        _searchQuery.value = newQuery
    }

    // # Address Management
    fun addAddress(address: UserAddress) {
        _addresses.value += address
    }

    fun deleteAddress(address: UserAddress) {
        _addresses.value -= address
    }

    fun updateAddress(address: UserAddress) {
        _addresses.value = _addresses.value.map { if (it.id == address.id) address else it }
    }

    private val _orderToSubmit = MutableStateFlow<Order?>(null)
    val orderToSubmit = _orderToSubmit.asStateFlow()
    fun addOrderToSubmit(order: Order) {
//        _orderToSubmit.value = order
        _orders.value += order
    }

    fun getOrderById(orderId: String): Order?  {
      return  _orders.value.find { it.id == orderId }
    }
}

