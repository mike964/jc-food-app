package com.example.gmfastfood.data



data class CartItem(
    val id: Int,
    val title: String,
    val description: String = "",
    val price: Double,
    val imageUrl : String?  ,
    val quantity: Int = 1,
) {
    // Helper to get total price for this specific item
    val totalPrice: Double get() = price * quantity
}

data class UserAddress(
    val id: String,
    val label: String,
//    val streetAddress: String,
    val fullAddress: String,
    val note: String = "",
//    val notesToDriver: String?,
    val latitude: Double?,
    val longitude: Double?,
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

enum class OrderStatus(val displayName: String) {
    PENDING("Pending"),
    SHIPPED("Shipped"),
    DELIVERED("Delivered"),
    CANCELLED("Cancelled")

}

data class OrderItem(
    val id: String,
    val title: String,
    val quantity: Int,
    val price: Double,
)
data class OrderItem2(
    val id: String,
    val restaurantOrStoreName: String,
    val itemsSummary: String,
    val totalPrice: Double,
    val status: OrderStatus, //  PLACED, PREPARING, OUT_FOR_DELIVERY, DELIVERED, CANCELLED;
    val dateString: String
)


data class Order(
    val id: String,
    val date: String,
    val status: String,
    val items: List<CartItem>,
    val subtotal: Double,
    val shipping: Double,
    val tax: Double,
    val total: Double,
    val address: String,
    val note: String
)

// Expected model (matching your Edit Screen setup)
