package com.example.myapplication

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Client for making API calls
object ApiClient {
    // Create Retrofit instance
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://fakestoreapi.com/")  // Base URL for the API
        .addConverterFactory(GsonConverterFactory.create())  // Use Gson for JSON conversion
        .build()

    // Create API service
    val api = retrofit.create(ApiService::class.java)
}