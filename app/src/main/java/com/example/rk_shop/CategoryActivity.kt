package com.example.rk_shop

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class CategoryActivity : AppCompatActivity() {

    private lateinit var textViewCategories: TextView
    private lateinit var buttonViewProducts: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        textViewCategories = findViewById(R.id.textViewCategories)
        buttonViewProducts = findViewById(R.id.buttonViewProducts)

        buttonViewProducts.setOnClickListener {
            // Handle view products logic
        }
    }
}
