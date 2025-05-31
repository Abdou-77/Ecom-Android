package com.example.myapplication

/**
 * Represents a product in the e-commerce application.
 *
 * This data class contains all the information about a product including its
 * basic details, pricing, and rating information.
 *
 * @property id Unique identifier for the product
 * @property title Name of the product
 * @property price Current price of the product in USD
 * @property description Detailed description of the product
 * @property category Category the product belongs to
 * @property image URL of the product's image
 * @property rating Product rating information
 */
data class Product(
    val id: Int,           // Product ID
    val title: String,     // Product name
    val price: Double,     // Product price
    val description: String, // Product description
    val category: String,  // Product category
    val image: String,     // Product image URL
    val rating: Rating = Rating(0.0, 0)  // Product rating with default values
)

/**
 * Represents the rating information for a product.
 *
 * @property rate Average rating of the product (0.0 to 5.0)
 * @property count Total number of ratings received
 */
data class Rating(
    val rate: Double,  // Rating value (0-5)
    val count: Int     // Number of reviews
) 