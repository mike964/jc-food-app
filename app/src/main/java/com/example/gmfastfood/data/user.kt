package com.example.gmfastfood.data

import com.example.gmfastfood.order.OrderItem
import com.example.gmfastfood.order.OrderStatus


//phone, password, email , full name, addresses [], orders, favorite items list.

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val phoneNumber: String,
    val isAdmin: Boolean = false,
    val address: String,
    val orders: List<Order>,
    val favoriteItems: List<String>,
)

data class Order(
    val orderId: String,
    val date: String,
    val status: OrderStatus,
    val items: List<OrderItem>,
    val totalAmount: Double
)

/*
data class Order(
    val id: Int,
    val userId: Int,
//    val items: List<OrderItem>,
    val totalPrice: Double,
    val status: String,  // "pending", "processing", "shipped", "delivered", "cancelled"
//    "timestamp": "2026-05-28T09:47:00Z"
    val createdAt: String,
    val deliveredAt: String,
    val paymentMethod: String,
    val deliveryAddress: String,
    val itemsList: List<String>,
    val note: String,
    val subtotal: Double,
    val deliveryFee: Double,
    val discount: Double,
    val paymentStatus: String,
    val delivered: Boolean,
)

 */

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

/*
var sampleOrders = listOf(
    Order(
        1, 1, 15500.0, "Delivered",
        "1780023153", "", "",
        "",
        listOf("Burger", "Pizza", "Salad"),
        "Note",
        13000.0,
        2000.0,
        0.0,
        "Paid",
        true
    ),
    Order(
        2, 1, 17500.0, "Delivered",
        "1780003153", "", "",
        "",
        listOf("Burger", "Pepsi", "Salad"),
        "Note",
        15500.0,
        2000.0,
        0.0,
        "Paid",
        true
    ),

    //, Order(), Order(), Order(), Order(), Order()
)

 */

val sampleOrders2 = listOf(
    Order(
        orderId = "98231",
        date = "May 28, 2026",
        status = OrderStatus.PENDING,
        items = listOf(
            OrderItem("1", "Wireless Noise-Canceling Headphones", 1, 199.99),
            OrderItem("2", "USB-C Charging Cable 2m", 2, 15.49)
        ),
        totalAmount = 230.97
    ),
    Order(
        orderId = "97140",
        date = "May 24, 2026",
        status = OrderStatus.DELIVERED,
        items = listOf(
            OrderItem("3", "Ergonomic Mechanical Keyboard", 1, 129.00)
        ),
        totalAmount = 129.00
    )
)