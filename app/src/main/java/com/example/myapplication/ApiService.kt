package com.example.myapplication

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

// Interface for API calls
interface ApiService {
    // Get all products
    @GET("products")
    fun getProducts(): Call<List<Product>>

    // Get a single product by ID
    @GET("products/{id}")
    fun getProduct(@Path("id") id: Int): Call<Product>
}