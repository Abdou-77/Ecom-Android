package com.example.myapplication

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.button.MaterialButton
import com.google.zxing.integration.android.IntentIntegrator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.graphics.Rect
import android.widget.TextView

// Main activity for the app
class MainActivity : AppCompatActivity() {
    // Variables for the app
    private lateinit var adapter: ProductAdapter
    private lateinit var list: RecyclerView
    private val CAMERA_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // Initialize cart
        CartManager.init(this)
        
        // Setup everything
        setupViews()
        setupSearch()
        setupNav()
        getProducts()
    }

    // Setup all the views
    private fun setupViews() {
        // Setup the product list
        list = findViewById(R.id.productsRecyclerView)
        val grid = GridLayoutManager(this, 2)
        list.layoutManager = grid
        
        // Add spacing between items
        list.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                val space = resources.getDimensionPixelSize(R.dimen.grid_spacing)
                outRect.left = space
                outRect.right = space
                outRect.top = space
                outRect.bottom = space
            }
        })

        // Setup adapter
        adapter = ProductAdapter(emptyList()) { product ->
            showProduct(product.id)
        }
        list.adapter = adapter

        // Setup cart button
        findViewById<MaterialButton>(R.id.cartButton).setOnClickListener {
            showCart()
        }

        // Setup search
        setupSearch()

        // Setup QR scanner
        findViewById<FloatingActionButton>(R.id.scanFab).setOnClickListener {
            checkCamera()
        }
    }

    // Setup bottom navigation
    private fun setupNav() {
        findViewById<BottomNavigationView>(R.id.bottomNavigation).setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    // Refresh products
                    findViewById<android.widget.EditText>(R.id.searchInput).text.clear()
                    showToast("Refreshing...")
                    getProducts()
                    true
                }
                else -> false
            }
        }
    }

    // Show a custom toast message
    private fun showToast(msg: String) {
        val inflater = LayoutInflater.from(this)
        val layout = inflater.inflate(R.layout.custom_toast, null)
        layout.findViewById<TextView>(R.id.toastText).text = msg
        
        val toast = Toast(applicationContext)
        toast.setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, 200)
        toast.duration = Toast.LENGTH_SHORT
        toast.view = layout
        toast.show()
    }

    // Setup search input
    private fun setupSearch() {
        val search = findViewById<android.widget.EditText>(R.id.searchInput)
        search.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapter.filter.filter(s)
            }
            override fun afterTextChanged(s: android.text.Editable?) {}
        })
    }

    // Check camera permission and start scanner
    private fun checkCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_CODE)
        } else {
            startScanner()
        }
    }

    // Start QR scanner
    private fun startScanner() {
        val scanner = IntentIntegrator(this)
        scanner.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        scanner.setPrompt("Scan a product QR code")
        scanner.setCameraId(0)
        scanner.setBeepEnabled(false)
        scanner.setOrientationLocked(true)
        scanner.setCaptureActivity(CaptureActivity::class.java)
        scanner.initiateScan()
    }

    // Handle QR scan result
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents != null) {
                try {
                    val id = result.contents.toInt()
                    showProduct(id)
                } catch (e: NumberFormatException) {
                    Toast.makeText(this, "Invalid QR code format", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    // Open product detail screen
    private fun showProduct(id: Int) {
        val intent = Intent(this, ProductDetailActivity::class.java).apply {
            putExtra(ProductDetailActivity.EXTRA_PRODUCT_ID, id)
        }
        startActivity(intent)
    }

    // Show cart screen
    private fun showCart() {
        val intent = Intent(this, CartActivity::class.java)
        startActivity(intent)
    }

    // Get products from API
    private fun getProducts() {
        adapter.showLoading()
        
        ApiClient.api.getProducts().enqueue(object : Callback<List<Product>> {
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                if (response.isSuccessful) {
                    response.body()?.let { products ->
                        adapter.updateData(products)
                    }
                } else {
                    showError("Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                showError("Network error: ${t.message}")
            }
        })
    }

    // Handle errors
    private fun showError(msg: String) {
        Log.e("MainActivity", msg)
        showToast(msg)
        adapter.showError()
    }
}