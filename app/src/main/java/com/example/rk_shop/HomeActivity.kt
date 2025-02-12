package com.example.rk_shop

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity() {

    private lateinit var buttonViewProducts: Button
    private lateinit var buttonViewWishlist: Button
    private lateinit var buttonViewCart: Button
    private lateinit var buttonProfile: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        buttonViewProducts = findViewById(R.id.buttonViewProducts)
        buttonViewWishlist = findViewById(R.id.buttonViewWishlist)
        buttonViewCart = findViewById(R.id.buttonViewCart)
        buttonProfile = findViewById(R.id.buttonProfile)

        buttonViewProducts.setOnClickListener {
            // Handle view products logic
        }

        buttonViewWishlist.setOnClickListener {
            // Handle view wishlist logic
        }

        buttonViewCart.setOnClickListener {
            // Handle view cart logic
        }

        buttonProfile.setOnClickListener {
            // Handle profile logic
        }
    }
}
