package com.example.rk_shop

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ProfileActivity : AppCompatActivity() {

    private lateinit var textViewUsername: TextView
    private lateinit var textViewEmail: TextView
    private lateinit var buttonEditProfile: Button
    private lateinit var buttonManageAddress: Button
    private lateinit var buttonPaymentMethod: Button
    private lateinit var buttonMyOrders: Button
    private lateinit var buttonMyCoupons: Button
    private lateinit var buttonNotifications: Button
    private lateinit var buttonSettings: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        textViewUsername = findViewById(R.id.textViewUsername)
        textViewEmail = findViewById(R.id.textViewEmail)
        buttonEditProfile = findViewById(R.id.buttonEditProfile)
        buttonManageAddress = findViewById(R.id.buttonManageAddress)
        buttonPaymentMethod = findViewById(R.id.buttonPaymentMethod)
        buttonMyOrders = findViewById(R.id.buttonMyOrders)
        buttonMyCoupons = findViewById(R.id.buttonMyCoupons)
        buttonNotifications = findViewById(R.id.buttonNotifications)
        buttonSettings = findViewById(R.id.buttonSettings)

        buttonEditProfile.setOnClickListener {
            // Handle edit profile logic
        }

        buttonManageAddress.setOnClickListener {
            // Handle manage address logic
        }

        buttonPaymentMethod.setOnClickListener {
            // Handle payment method logic
        }

        buttonMyOrders.setOnClickListener {
            // Handle my orders logic
        }

        buttonMyCoupons.setOnClickListener {
            // Handle my coupons logic
        }

        buttonNotifications.setOnClickListener {
            // Handle notifications logic
        }

        buttonSettings.setOnClickListener {
            // Handle settings logic
        }
    }
}
