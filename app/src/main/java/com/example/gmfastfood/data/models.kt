package com.example.gmfastfood.data

import com.example.gmfastfood.vm.CartItem



data class UserAddress(
    val id: String,
    val label: String,
//    val streetAddress: String,
    val fullAddress: String,
    val note: String = "",
//    val notesToDriver: String?,
//    val latitude: Double,
//    val longitude: Double,
    val isDefault: Boolean = false
)



data class FoodOrder(
    val orderId: String,
    val trackingNumber: String,
    val restaurantName: String,
    val restaurantId: String,
    val items: List<CartItem>,
    val totalPaid: Double,
    val address: UserAddress,
    val status: FoodOrderStatus,
    val timestampMillis: Long
)

enum class FoodOrderStatus {
    CONFIRMING,
    PREPARING,
    OUT_FOR_DELIVERY,
    ARRIVED,
    COMPLETED,
    CANCELLED
}

// Expected model (matching your Edit Screen setup)
