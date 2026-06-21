package com.example.gmfastfood.repository

import com.example.gmfastfood.data.FakeApiClient
import com.example.gmfastfood.data.Product



class ProductRepository(
 //private val apiService: ApiService
 private val apiService: FakeApiClient
) {
    //private val apiService: FakeApiClient = FakeApiClient()

    suspend fun getProducts(): List<Product> {
     return  apiService.getProductItems()
 }
}
