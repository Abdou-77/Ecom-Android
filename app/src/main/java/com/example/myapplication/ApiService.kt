package com.example.myapplication

import retrofit2.Call
import retrofit2.http.GET


interface ApiService {
    @GET("products")
    fun getAllProducts(): Call<List<Product>> // Now uses retrofit2.Call
}