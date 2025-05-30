package com.example.myapplication

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton

class CartActivity : AppCompatActivity() {
    private lateinit var adapter: CartAdapter
    private lateinit var totalText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        setupToolbar()
        setupViews()
        loadCartItems()
    }

    private fun setupToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        
        // Setup back button
        findViewById<ImageButton>(R.id.backButton).setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupViews() {
        totalText = findViewById(R.id.totalText)

        adapter = CartAdapter { product ->
            CartManager.removeFromCart(product)
            loadCartItems()
        }

        findViewById<RecyclerView>(R.id.cartRecyclerView).apply {
            layoutManager = LinearLayoutManager(this@CartActivity)
            adapter = this@CartActivity.adapter
        }

        findViewById<MaterialButton>(R.id.checkoutButton).setOnClickListener {
            showCustomToast("Done")
        }
    }

    private fun showCustomToast(message: String) {
        val inflater = LayoutInflater.from(this)
        val layout = inflater.inflate(R.layout.custom_toast, null)
        
        layout.findViewById<TextView>(R.id.toastText).text = message
        
        val toast = Toast(applicationContext)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.duration = Toast.LENGTH_SHORT
        toast.view = layout
        toast.show()
    }

    private fun loadCartItems() {
        val cartItems = CartManager.getCartItems()
        adapter.updateItems(cartItems)
        val totalLayout = findViewById<View>(R.id.totalLayout)
        if (cartItems.isEmpty()) {
            totalLayout.visibility = View.GONE
        } else {
            totalLayout.visibility = View.VISIBLE
            totalText.text = "Total: $${String.format("%.2f", CartManager.getCartTotal())}"
        }
    }
} 