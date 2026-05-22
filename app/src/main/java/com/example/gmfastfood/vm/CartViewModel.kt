package com.example.gmfastfood.vm

import androidx.lifecycle.ViewModel
import com.example.gmfastfood.R
import com.example.gmfastfood.data.products
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class CartItem(
    val id: Int,
    val name: String,
    val price: Double,
    val imageUrl: String = "",
    val quantity: Int = 1,
) {
    // Helper to get total price for this specific item
    val totalPrice: Double get() = price * quantity
}

// UI State wrapper
data class CartUiState(
    val cartItems: List<CartItem> = emptyList(),
    val isLoading: Boolean = false,
) {
    // Derived state for the summary
    val subtotal: Double get() = cartItems.sumOf { it.totalPrice }
    val shipping: Double get() = if (subtotal > 0 && subtotal < 50.0) 5.0 else 0.0
    val total: Double get() = subtotal + shipping
}

class CartViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CartUiState())
    val uiState: StateFlow<CartUiState> = _uiState.asStateFlow()

    init {
        // Initialize with some dummy data for demonstration
        loadCartItems()
    }

    private fun loadCartItems() {
        _uiState.update {
            it.copy(
                cartItems = listOf(
                    CartItem(1, "Burger", 2000.0, R.drawable.burger.toString()),
                    CartItem(2, "Pizza", 2000.0, R.drawable.pizza.toString())
                )
            )
        }
    }

    fun addToCart(item: CartItem) {
        _uiState.update { currentState ->
            val existingItem = currentState.cartItems.find { it.id == item.id }
            if (existingItem != null) {
                val updatedList = currentState.cartItems.map {
                    if (it.id == item.id) it.copy(quantity = it.quantity + 1) else it
                }
                currentState.copy(cartItems = updatedList)
            } else {
                currentState.copy(cartItems = currentState.cartItems + item)
            }
        }
    }

    fun updateQuantity(itemId: Int, newQuantity: Int) {
        if (newQuantity <= 0) {
            removeItem(itemId)
            return
        }

        _uiState.update { currentState ->
            val updatedList = currentState.cartItems.map { item ->
                if (item.id == itemId) item.copy(quantity = newQuantity) else item
            }
            currentState.copy(cartItems = updatedList)
        }
    }

    fun removeItem(itemId: Int) {
        _uiState.update { currentState ->
            val updatedList = currentState.cartItems.filterNot { it.id == itemId }
            currentState.copy(cartItems = updatedList)
        }
    }

    fun clearCart() {
        _uiState.update { it.copy(cartItems = emptyList()) }
    }
}
