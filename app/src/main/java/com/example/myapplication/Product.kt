package com.example.myapplication

// Class for product ratings
data class Rating(
    val rate: Double,  // Rating value (0-5)
    val count: Int     // Number of reviews
)

// Class for products
data class Product(
    val id: Int,           // Product ID
    val title: String,     // Product name
    val price: Double,     // Product price
    val description: String, // Product description
    val category: String,  // Product category
    val image: String,     // Product image URL
    val rating: Rating = Rating(0.0, 0)  // Product rating with default values
) 