package com.example.gmfastfood.data

val sampleAddresses = listOf(
    UserAddress(
        id = "1",
        label = "Home",
        fullAddress = "123 Main St, Apt 4B",
        deliveryNotes = "Ring bell twice, leave at reception",
        isDefault = true
    ),
    UserAddress(
        id = "2",
        label = "Work",
        fullAddress = "324 Main St, Apt 2C",
        deliveryNotes = "No need for extra stuff",
        isDefault = false
    )
)