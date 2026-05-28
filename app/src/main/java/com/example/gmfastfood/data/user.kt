package com.example.gmfastfood.data


//phone, password, email , full name, addresses [], orders, favorite items list.

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val phoneNumber: String,
    val isAdmin: Boolean = false,
    val address : String,
    val orders : List<Order>,
    val favoriteItems : List<String>,
)

data class Order(
    val id: Int,
    val user: User,
//    val items: List<OrderItem>,
    val total: Double,
    val status: String,  // "pending", "processing", "shipped", "delivered", "cancelled"
//    "timestamp": "2026-05-28T09:47:00Z"
    val createdAt: String,
    val deliveredAt : String,
    val paymentMethod: String,
    val deliveryAddress: String,
    val itemsList : List<String>,
    val note : String,
    val subtotal : Double,
    val deliveryFee : Double,
    val discount : Double,
    val finalTotal : Double,
    val paymentStatus : String,
    val delivered : Boolean,
)

val user1 = User(
    id = 1,
    name = "Hasan Ali",
    email = "hasan@mail.com",
    phoneNumber = "1234567890",
    isAdmin = true,
    address = "123 Main St",
    orders = listOf(),
    favoriteItems = listOf(),
)
