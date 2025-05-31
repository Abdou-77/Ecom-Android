package com.example.myapplication

import android.os.Bundle
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton

/**
 * Activity that displays detailed information about a product.
 * 
 * This activity is responsible for:
 * - Displaying product details including title, price, description, and image
 * - Showing product rating and review count
 * - Handling "Add to Cart" functionality
 * 
 * @property EXTRA_PRODUCT_ID The key used to pass the product ID through the intent
 */
class ProductDetailActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_PRODUCT_ID = "extra_product_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        val productId = intent.getIntExtra(EXTRA_PRODUCT_ID, -1)
        if (productId == -1) {
            finish()
            return
        }

        setupToolbar()
        fetchProductDetails(productId)
    }

    /**
     * Sets up the toolbar with navigation functionality.
     * Configures the toolbar to show a back button and handle navigation events.
     */
    private fun setupToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    /**
     * Fetches product details from the API.
     * 
     * @param productId The ID of the product to fetch
     */
    private fun fetchProductDetails(productId: Int) {
        ApiClient.api.getProduct(productId).enqueue(object : retrofit2.Callback<Product> {
            override fun onResponse(call: retrofit2.Call<Product>, response: retrofit2.Response<Product>) {
                if (response.isSuccessful) {
                    response.body()?.let { product ->
                        displayProductDetails(product)
                    }
                }
            }

            override fun onFailure(call: retrofit2.Call<Product>, t: Throwable) {
                // Handle error
            }
        })
    }

    /**
     * Displays the product details in the UI.
     * 
     * @param product The product object containing all the details to display
     */
    private fun displayProductDetails(product: Product) {
        findViewById<TextView>(R.id.productTitle).text = product.title
        findViewById<TextView>(R.id.productPrice).text = "$${product.price}"
        findViewById<TextView>(R.id.productDescription).text = product.description
        
        // Update rating display
        findViewById<RatingBar>(R.id.productRating).rating = product.rating.rate.toFloat()
        findViewById<TextView>(R.id.ratingCount).text = "(${product.rating.count})"

        Glide.with(this)
            .load(product.image)
            .centerCrop()
            .into(findViewById(R.id.productImage))

        findViewById<MaterialButton>(R.id.addToCartButton).setOnClickListener {
            CartManager.addToCart(product)
            finish()
        }
    }
} 