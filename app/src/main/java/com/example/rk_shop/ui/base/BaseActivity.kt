package com.example.rk_shop.ui.base

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewbinding.ViewBinding
import com.example.rk_shop.util.ThemeManager
import com.google.android.material.transition.platform.MaterialArcMotion
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {
    
    protected lateinit var binding: VB
    protected abstract fun getViewBinding(): VB

    override fun onCreate(savedInstanceState: Bundle?) {
        setupTransitions()
        super.onCreate(savedInstanceState)
        
        binding = getViewBinding()
        setContentView(binding.root)
        
        ThemeManager.setupEdgeToEdge(this)
        setupWindowInsets()
        setupViews(savedInstanceState)
    }

    private fun setupTransitions() {
        window.sharedElementEnterTransition = buildContainerTransform(true)
        window.sharedElementReturnTransition = buildContainerTransform(false)
        
        // Set up activity transitions
        findViewById<View>(android.R.id.content).transitionName = "root_transition"
        setEnterSharedElementCallback(MaterialContainerTransformSharedElementCallback())
    }

    private fun buildContainerTransform(entering: Boolean): MaterialContainerTransform {
        return MaterialContainerTransform().apply {
            addTarget(android.R.id.content)
            duration = 400L
            pathMotion = MaterialArcMotion()
            isElevationShadowEnabled = true
            startElevation = 0f
            endElevation = 8f
            scrimColor = android.graphics.Color.TRANSPARENT
        }
    }

    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            
            // Apply padding to the root view
            (view as? ViewGroup)?.let { rootView ->
                rootView.setPadding(
                    insets.left,
                    0, // Don't apply top padding as it will be handled by individual views
                    insets.right,
                    0  // Don't apply bottom padding as it will be handled by individual views
                )
            }

            // Handle insets for specific views
            handleWindowInsets(insets)
            
            WindowInsetsCompat.CONSUMED
        }
    }

    /**
     * Override this method to handle window insets for specific views
     */
    protected open fun handleWindowInsets(insets: androidx.core.graphics.Insets) {
        // To be implemented by child classes
    }

    /**
     * Override this method to setup views
     */
    protected open fun setupViews(savedInstanceState: Bundle?) {
        // To be implemented by child classes
    }

    /**
     * Helper method to apply shared element transitions
     */
    protected fun prepareSharedElementTransition(
        sharedElement: View,
        transitionName: String
    ) {
        sharedElement.transitionName = transitionName
        supportPostponeEnterTransition()
        
        sharedElement.viewTreeObserver.addOnPreDrawListener {
            supportStartPostponedEnterTransition()
            true
        }
    }

    /**
     * Helper method to start an activity with shared element transition
     */
    protected fun startActivityWithTransition(
        intent: android.content.Intent,
        sharedElement: View,
        transitionName: String
    ) {
        val options = android.app.ActivityOptions.makeSceneTransitionAnimation(
            this,
            sharedElement,
            transitionName
        )
        startActivity(intent, options.toBundle())
    }

    /**
     * Helper method to change theme
     */
    protected fun changeTheme(themeMode: ThemeManager.ThemeMode) {
        ThemeManager.setTheme(this, themeMode, animate = true)
    }

    /**
     * Helper method to apply fade animation to view
     */
    protected fun View.fadeIn(duration: Long = 300) {
        alpha = 0f
        visibility = View.VISIBLE
        animate()
            .alpha(1f)
            .setDuration(duration)
            .setInterpolator(android.view.animation.AccelerateDecelerateInterpolator())
            .start()
    }

    protected fun View.fadeOut(duration: Long = 300, gone: Boolean = false) {
        animate()
            .alpha(0f)
            .setDuration(duration)
            .setInterpolator(android.view.animation.AccelerateDecelerateInterpolator())
            .withEndAction {
                visibility = if (gone) View.GONE else View.INVISIBLE
            }
            .start()
    }
}
