package com.example.rk_shop

import android.app.Application
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import com.example.rk_shop.util.ThemeManager
import com.google.android.material.color.DynamicColors
import com.google.android.material.color.MaterialColors

class RKShopApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        
        setupTheme()
        setupDynamicColors()
    }

    private fun setupTheme() {
        // Initialize theme manager
        ThemeManager.init(this)

        // Enable edge-to-edge by default
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }

    private fun setupDynamicColors() {
        // Apply dynamic colors if available (Android 12+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            DynamicColors.applyToActivitiesIfAvailable(this)
        }
    }

    companion object {
        private const val TAG = "RKShopApplication"
    }
}
