package com.example.rk_shop

import android.os.Bundle
import android.widget.Button
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {

    private lateinit var switchNotifications: Switch
    private lateinit var buttonChangePassword: Button
    private lateinit var buttonLogout: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        switchNotifications = findViewById(R.id.switchNotifications)
        buttonChangePassword = findViewById(R.id.buttonChangePassword)
        buttonLogout = findViewById(R.id.buttonLogout)

        buttonChangePassword.setOnClickListener {
            // Handle change password logic
        }

        buttonLogout.setOnClickListener {
            // Handle logout logic
        }

        // Load user settings
        loadUserSettings()
    }

    private fun loadUserSettings() {
        // Logic to load user settings from the backend and update the UI
        // For example, fetching notification settings from a database or API
    }
}
