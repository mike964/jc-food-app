package com.example.gmfastfood.vm

import androidx.lifecycle.ViewModel
import com.example.gmfastfood.R
import com.example.gmfastfood.data.CartItem
import com.example.gmfastfood.data.products
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update



// UI State wrapper
data class CartUiState(
    val cartItems: List<CartItem> = emptyList(),
    val isLoading: Boolean = false,
//    val subtotal: Double,
    val deliveryFee: Double = 2000.0,
//    val total: Double
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
//        loadCartItems()  // Just for developer test
    }

    private fun loadCartItems() {
        _uiState.update {
            it.copy(
                cartItems = listOf(
                    CartItem(56, "Double Cheeseburger", "Flame-grilled beef, cheddar, special sauce", 8500.0, R.drawable.burger.toString(), 2),
                    CartItem(57, "Crispy French Fries", "Salted, golden brown potato fries", 3500.0, "", 1),
                    CartItem(58, "Strawberry Milkshake", "Made with real organic ice cream", 4500.0, "", 1)
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

    private fun updateCartTotals(items: List<CartItem>) {
        val subtotal = items.sumOf { it.price * it.quantity }
        val deliveryFee = if (subtotal > 20.0) 0.0 else 3.99 // Free delivery over $20
        val total = subtotal + deliveryFee

        _uiState.update { currentState ->
            currentState.copy(
                cartItems = items,
//                subtotal = subtotal,
                deliveryFee = deliveryFee,
//                total = total
            )
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
