package com.example.rk_shop

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class CartActivity : AppCompatActivity() {

    private lateinit var textViewTotalPrice: TextView
    private lateinit var buttonCheckout: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        textViewTotalPrice = findViewById(R.id.textViewTotalPrice)
        buttonCheckout = findViewById(R.id.buttonCheckout)

        buttonCheckout.setOnClickListener {
        // Handle checkout logic
        proceedToCheckout()
        }

        // Load cart items and calculate total price
        loadCartItems()
    }

    private fun loadCartItems() {
        // Logic to load cart items from the backend and update the UI
        textViewTotalPrice.text = "Total Price: $0.00" // Placeholder for total price
    }
}
