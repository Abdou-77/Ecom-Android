package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton

class ProductAdapter(private var products: List<Product>) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>(), Filterable {

    private var filteredList: List<Product> = products

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productImage: ImageView = itemView.findViewById(R.id.productImage)
        val productTitle: TextView = itemView.findViewById(R.id.productTitle)
        val productPrice: TextView = itemView.findViewById(R.id.productPrice)
        val productRating: RatingBar = itemView.findViewById(R.id.productRating)
        val ratingCount: TextView = itemView.findViewById(R.id.ratingCount)
        val addToCartButton: MaterialButton = itemView.findViewById(R.id.addToCartButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = filteredList[position]
        holder.productTitle.text = product.title
        holder.productPrice.text = "$${product.price}"
        
        // Set rating
        holder.productRating.rating = product.rating.rate.toFloat()
        holder.ratingCount.text = "(${product.rating.count})"

        // Load image with Glide
        Glide.with(holder.itemView.context)
            .load(product.image)
            .centerCrop()
            .into(holder.productImage)

        // Set click listener for add to cart button
        holder.addToCartButton.setOnClickListener {
            // TODO: Implement add to cart functionality
        }
    }

    override fun getItemCount(): Int = filteredList.size

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filtered = if (constraint.isNullOrEmpty()) {
                    products
                } else {
                    products.filter { 
                        it.title.contains(constraint, true) || 
                        it.description.contains(constraint, true) 
                    }
                }
                return FilterResults().apply { values = filtered }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredList = results?.values as? List<Product> ?: emptyList()
                notifyDataSetChanged()
            }
        }
    }

    fun updateData(newProducts: List<Product>) {
        products = newProducts
        filteredList = newProducts
        notifyDataSetChanged()
    }
}
