package com.example.rk_shop.util

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.animation.doOnEnd
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import kotlin.math.hypot
import com.example.rk_shop.R

object ThemeManager {
    private const val PREFERENCES_NAME = "theme_preferences"
    private const val KEY_THEME_MODE = "theme_mode"
    
    private lateinit var preferences: SharedPreferences
    private var currentTheme: ThemeMode = ThemeMode.SYSTEM
    
    enum class ThemeMode {
        LIGHT, DARK, SYSTEM
    }

    fun init(context: Context) {
        preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
        currentTheme = ThemeMode.valueOf(
            preferences.getString(KEY_THEME_MODE, ThemeMode.SYSTEM.name) ?: ThemeMode.SYSTEM.name
        )
        applyTheme(currentTheme)
    }

    fun getCurrentTheme(): ThemeMode = currentTheme

    fun setTheme(activity: Activity, themeMode: ThemeMode, animate: Boolean = true) {
        if (currentTheme == themeMode) return

        preferences.edit().putString(KEY_THEME_MODE, themeMode.name).apply()
        currentTheme = themeMode

        if (animate) {
            animateThemeChange(activity) {
                applyTheme(themeMode)
            }
        } else {
            applyTheme(themeMode)
        }
    }

    private fun applyTheme(themeMode: ThemeMode) {
        when (themeMode) {
            ThemeMode.LIGHT -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            ThemeMode.DARK -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            ThemeMode.SYSTEM -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }

    private fun animateThemeChange(activity: Activity, onAnimationEnd: () -> Unit) {
        // Create circular reveal animation
        val rootView = activity.findViewById<ViewGroup>(android.R.id.content).getChildAt(0)
        val startView = activity.findViewById<View>(R.id.themeToggleButton) ?: rootView

        val startX = (startView.left + startView.right) / 2
        val startY = (startView.top + startView.bottom) / 2

        val endRadius = hypot(
            maxOf(startX, rootView.width - startX).toFloat(),
            maxOf(startY, rootView.height - startY).toFloat()
        )

        // Create overlay view for animation
        val overlayView = View(activity).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            setBackgroundColor(activity.getThemeColor(com.google.android.material.R.attr.colorSurface))
            alpha = 0f
        }
        rootView.addView(overlayView)

        // Animate overlay fade in
        ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 300
            addUpdateListener { animator ->
                overlayView.alpha = animator.animatedValue as Float
            }
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    onAnimationEnd.invoke()
                    
                    // Animate overlay fade out
                    ValueAnimator.ofFloat(1f, 0f).apply {
                        duration = 300
                        addUpdateListener { animator ->
                            overlayView.alpha = animator.animatedValue as Float
                        }
                        doOnEnd {
                            rootView.removeView(overlayView)
                        }
                        start()
                    }
                }
            })
            start()
        }
    }

    fun setupEdgeToEdge(activity: AppCompatActivity) {
        with(activity) {
            WindowCompat.setDecorFitsSystemWindows(window, false)
            
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { view, windowInsets ->
                val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
                view.setPadding(insets.left, insets.top, insets.right, insets.bottom)
                windowInsets
            }

            updateSystemBarsAppearance(window)
        }
    }

    private fun updateSystemBarsAppearance(window: Window) {
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.apply {
            // Show system bars by default
            show(WindowInsetsCompat.Type.systemBars())
            
            // Configure system bars behavior
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            
            // Set system bars appearance based on theme
            val isDarkTheme = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
            isAppearanceLightStatusBars = !isDarkTheme
            isAppearanceLightNavigationBars = !isDarkTheme
        }

        // Make system bars translucent
        window.apply {
            statusBarColor = android.graphics.Color.TRANSPARENT
            navigationBarColor = android.graphics.Color.TRANSPARENT
            
            // Disable system bar dim
            setFlags(
                WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS,
                WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS or
                        WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS or
                        WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
            )
        }
    }
}

// Extension function to get color from theme attribute
fun Context.getThemeColor(attr: Int): Int {
    val typedArray = theme.obtainStyledAttributes(intArrayOf(attr))
    val color = typedArray.getColor(0, 0)
    typedArray.recycle()
    return color
}
