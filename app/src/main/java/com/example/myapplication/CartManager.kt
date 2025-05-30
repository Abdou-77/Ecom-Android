package com.example.myapplication

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object CartManager {
    private const val PREF_NAME = "cart_prefs"
    private const val KEY_CART_ITEMS = "cart_items"
    private lateinit var prefs: SharedPreferences
    private val gson = Gson()

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun addToCart(product: Product) {
        val cartItems = getCartItems().toMutableList()
        cartItems.add(product)
        saveCartItems(cartItems)
    }

    fun removeFromCart(product: Product) {
        val cartItems = getCartItems().toMutableList()
        cartItems.remove(product)
        saveCartItems(cartItems)
    }

    fun getCartItems(): List<Product> {
        val json = prefs.getString(KEY_CART_ITEMS, null) ?: return emptyList()
        val type = object : TypeToken<List<Product>>() {}.type
        return gson.fromJson(json, type)
    }

    fun clearCart() {
        saveCartItems(emptyList())
    }

    fun getCartTotal(): Double {
        return getCartItems().sumOf { it.price }
    }

    private fun saveCartItems(items: List<Product>) {
        val json = gson.toJson(items)
        prefs.edit().putString(KEY_CART_ITEMS, json).apply()
    }
} 