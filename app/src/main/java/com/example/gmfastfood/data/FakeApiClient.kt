package com.example.gmfastfood.data

import kotlinx.coroutines.delay
import java.io.IOException

class FakeApiClient {
    private var toggleErrorFlag = false // Flag to simulate random network dropouts

    suspend fun getProductItems(): List<Product> {
        // 1. Simulate network latency (2.5 seconds delay)
        delay(2500)

        // 2. Intermittent error injection to test UI resilience
//        toggleErrorFlag = !toggleErrorFlag

        if (toggleErrorFlag) {
            throw IOException("Unable to reach store server. Please check your network connection.")
        }

        // 3. Mock database payload data returned on success paths
        return products.shuffled()
    }

    fun toggleError() {
        toggleErrorFlag = !toggleErrorFlag
    }

    fun resetError() {
        toggleErrorFlag = false

    }

    fun getItemById(id: Int): Product? = products.find { it.id == id }

    fun getProductsByCategory(category: String): List<Product> = products.filter { it.category == category }
}