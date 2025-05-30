package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton

class CartAdapter(
    private val onRemoveClick: (Product) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    private var items: List<Product> = emptyList()

    class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productImage: ImageView = itemView.findViewById(R.id.productImage)
        val productTitle: TextView = itemView.findViewById(R.id.productTitle)
        val productPrice: TextView = itemView.findViewById(R.id.productPrice)
        val removeButton: MaterialButton = itemView.findViewById(R.id.removeButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val product = items[position]
        
        holder.productTitle.text = product.title
        holder.productPrice.text = "$${product.price}"

        Glide.with(holder.itemView.context)
            .load(product.image)
            .centerCrop()
            .into(holder.productImage)

        holder.removeButton.setOnClickListener {
            onRemoveClick(product)
        }
    }

    override fun getItemCount(): Int = items.size

    fun updateItems(newItems: List<Product>) {
        items = newItems
        notifyDataSetChanged()
    }
} 