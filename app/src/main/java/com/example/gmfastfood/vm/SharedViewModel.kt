package com.example.gmfastfood.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gmfastfood.data.ApiItem
import com.example.gmfastfood.data.FakeApiClient
import com.example.gmfastfood.data.FakeStoreApiClient
import com.example.gmfastfood.data.Order
import com.example.gmfastfood.data.Product
import com.example.gmfastfood.data.StoreApiService
import com.example.gmfastfood.data.UserAddress
import com.example.gmfastfood.data.sampleAddresses
import com.example.gmfastfood.data.sampleOrders2
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class Book(val id: Int, val title: String, val author: String)

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

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders = _orders.asStateFlow()

    private val _addresses = MutableStateFlow<List<UserAddress>>(emptyList())
    val addresses = _addresses.asStateFlow()

    init {
        loadDataFromNetwork()
        loadInitialAddresses()
    }

    fun loadDataFromNetwork() {
        viewModelScope.launch {
            _uiState.update { UiState.Loading }
            try {
                val data = apiService.getProductItems()
                _uiState.update {
                    UiState.Success(items = data)
                }
                _products.value = data
                _orders.value = sampleOrders2
            } catch (e: Exception) {
                _uiState.update { UiState.Error(message = e.localizedMessage ?: "Unknown Error") }
            }
        }
    }

    fun loadInitialAddresses () {
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

    // 2. The backing dummy data (or data from a repository)
    private val _books = MutableStateFlow(
        listOf(
            Book(1, "The Hobbit", "J.R.R. Tolkien"),
            Book(2, "1984", "George Orwell"),
            Book(3, "The Fellowship of the Ring", "J.R.R. Tolkien"),
            Book(4, "Animal Farm", "George Orwell"),
            Book(5, "Animal Kingdom", "George Orwell"),
            Book(6, "Thriving Farm", "George Washington")
        )
    )

    // 3. The filtered list state, updating dynamically
    val filteredBooks = combine(_books, _searchQuery) { books, query ->
        if (query.isBlank()) {
            books
        } else {
            books.filter { book ->
                book.title.contains(query, ignoreCase = true) ||
                        book.author.contains(query, ignoreCase = true)
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

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
    fun addAddress (address: UserAddress) {
        _addresses.value += address
        }

    fun deleteAddress (address: UserAddress) {
        _addresses.value -= address
    }

    fun updateAddress (address: UserAddress) {
        _addresses.value = _addresses.value.map { if (it.id == address.id) address else it }
    }

    private val _orderToSubmit = MutableStateFlow<Order?>(null)
    val orderToSubmit = _orderToSubmit.asStateFlow()
    fun addOrderToSubmit (order: Order) {
        _orderToSubmit.value = order
        _orders.value += order
    }

}