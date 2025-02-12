package com.example.rk_shop

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var imageViewProduct: ImageView
    private lateinit var textViewProductName: TextView
    private lateinit var textViewProductDescription: TextView
    private lateinit var textViewProductPrice: TextView
    private lateinit var buttonAddToCart: Button
    private lateinit var buttonAddToWishlist: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        imageViewProduct = findViewById(R.id.imageViewProduct)
        textViewProductName = findViewById(R.id.textViewProductName)
        textViewProductDescription = findViewById(R.id.textViewProductDescription)
        textViewProductPrice = findViewById(R.id.textViewProductPrice)
        buttonAddToCart = findViewById(R.id.buttonAddToCart)
        buttonAddToWishlist = findViewById(R.id.buttonAddToWishlist)

        // Load product details
        loadProductDetails()

        buttonAddToCart.setOnClickListener {
            // Handle add to cart logic
        }

        buttonAddToWishlist.setOnClickListener {
            // Handle add to wishlist logic
        }
    }

    private fun loadProductDetails() {
        // Logic to load product details from the backend and update the UI
        textViewProductName.text = "Sample Product Name"
        textViewProductDescription.text = "Sample Product Description"
        textViewProductPrice.text = "Price: $99.99"
        // Load product image into imageViewProduct
    }
}
