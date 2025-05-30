package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton

// Adapter for the product list
class ProductAdapter(
    private var products: List<Product>,
    private val onAddToCart: (Product) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {

    // List for filtered products
    private var filteredList: List<Product> = products
    private var isLoading = false
    private var hasError = false

    // Different types of views
    companion object {
        private const val TYPE_PRODUCT = 0
        private const val TYPE_LOADING = 1
        private const val TYPE_ERROR = 2
    }

    // View holder for product items
    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img: ImageView = itemView.findViewById(R.id.productImage)
        val name: TextView = itemView.findViewById(R.id.productTitle)
        val price: TextView = itemView.findViewById(R.id.productPrice)
        val rating: RatingBar = itemView.findViewById(R.id.productRating)
        val reviews: TextView = itemView.findViewById(R.id.reviewCount)
        val addBtn: MaterialButton = itemView.findViewById(R.id.addToCartButton)
    }

    // View holder for loading state
    class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val progress: ProgressBar = itemView.findViewById(R.id.progressBar)
    }

    // View holder for error state
    class ErrorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val error: TextView = itemView.findViewById(R.id.errorText)
        val retry: MaterialButton = itemView.findViewById(R.id.retryButton)
    }

    // Create view holders
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_PRODUCT -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_product, parent, false)
                ProductViewHolder(view)
            }
            TYPE_LOADING -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_loading, parent, false)
                LoadingViewHolder(view)
            }
            TYPE_ERROR -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_error, parent, false)
                ErrorViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    // Bind data to views
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ProductViewHolder -> {
                val product = filteredList[position]
                holder.name.text = product.title
                holder.price.text = "$${product.price}"
                
                // Show rating
                holder.rating.rating = product.rating.rate.toFloat()
                holder.reviews.text = "(${product.rating.count})"

                // Load image
                Glide.with(holder.itemView.context)
                    .load(product.image)
                    .centerCrop()
                    .into(holder.img)

                // Add to cart button
                holder.addBtn.setOnClickListener {
                    onAddToCart(product)
                }
            }
            is ErrorViewHolder -> {
                holder.error.text = "Failed to load products"
                holder.retry.setOnClickListener {
                    // TODO: Add retry functionality
                }
            }
        }
    }

    // Get item count
    override fun getItemCount(): Int = when {
        isLoading -> 1
        hasError -> 1
        else -> filteredList.size
    }

    // Get view type
    override fun getItemViewType(position: Int): Int {
        return when {
            isLoading -> TYPE_LOADING
            hasError -> TYPE_ERROR
            else -> TYPE_PRODUCT
        }
    }

    // Filter for search
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filtered = if (constraint.isNullOrEmpty()) {
                    products
                } else {
                    products.filter { 
                        it.title.contains(constraint, true) || 
                        it.description.contains(constraint, true) ||
                        it.category.contains(constraint, true)
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

    // Update product list
    fun updateData(newProducts: List<Product>) {
        products = newProducts
        filteredList = newProducts
        isLoading = false
        hasError = false
        notifyDataSetChanged()
    }

    // Show loading state
    fun showLoading() {
        isLoading = true
        hasError = false
        notifyDataSetChanged()
    }

    // Show error state
    fun showError() {
        isLoading = false
        hasError = true
        notifyDataSetChanged()
    }

    // Find product by ID
    fun getProductById(id: Int): Product? {
        return products.find { it.id == id }
    }
}
