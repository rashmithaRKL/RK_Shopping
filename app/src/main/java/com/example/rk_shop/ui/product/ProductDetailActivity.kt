package com.example.rk_shop.ui.product

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.lifecycleScope
import com.example.rk_shop.R
import com.example.rk_shop.databinding.ActivityProductDetailBinding
import com.example.rk_shop.util.pressAnimation
import com.example.rk_shop.util.pulseAnimation
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductDetailBinding
    private var isFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        setupTransitions()
        super.onCreate(savedInstanceState)
        
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupMotionLayout()
        setupClickListeners()
        loadProductDetails()
    }

    private fun setupTransitions() {
        // Set up shared element transition
        findViewById<View>(android.R.id.content).transitionName = "product_container"
        setEnterSharedElementCallback(MaterialContainerTransformSharedElementCallback())
        
        window.sharedElementEnterTransition = MaterialContainerTransform().apply {
            addTarget(android.R.id.content)
            duration = 300L
        }
        window.sharedElementReturnTransition = MaterialContainerTransform().apply {
            addTarget(android.R.id.content)
            duration = 250L
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // Handle system insets
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            binding.motionLayout.setPadding(0, insets.top, 0, 0)
            WindowInsetsCompat.CONSUMED
        }
    }

    private fun setupMotionLayout() {
        binding.motionLayout.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionStarted(layout: MotionLayout?, startId: Int, endId: Int) {
                // Handle transition start
            }

            override fun onTransitionChange(layout: MotionLayout?, startId: Int, endId: Int, progress: Float) {
                // Update UI based on transition progress
                updateToolbarAlpha(progress)
            }

            override fun onTransitionCompleted(layout: MotionLayout?, currentId: Int) {
                // Handle transition completion
            }

            override fun onTransitionTrigger(layout: MotionLayout?, triggerId: Int, positive: Boolean, progress: Float) {
                // Handle transition trigger
            }
        })
    }

    private fun setupClickListeners() {
        binding.favoriteButton.setOnClickListener {
            it.pressAnimation()
            toggleFavorite()
        }

        binding.addToCartButton.setOnClickListener {
            it.pressAnimation()
            animateAddToCart()
        }

        binding.buyNowButton.setOnClickListener {
            it.pressAnimation()
            // Handle buy now action
        }
    }

    private fun toggleFavorite() {
        isFavorite = !isFavorite
        binding.favoriteButton.apply {
            isSelected = isFavorite
            pulseAnimation()
        }
    }

    private fun animateAddToCart() {
        binding.addToCartButton.isEnabled = false
        
        lifecycleScope.launch {
            binding.addToCartButton.text = getString(R.string.added_to_cart)
            binding.addToCartButton.icon = null
            binding.addToCartButton.pulseAnimation()
            
            delay(1500)
            
            binding.addToCartButton.text = getString(R.string.add_to_cart)
            binding.addToCartButton.setIconResource(R.drawable.ic_shopping_cart)
            binding.addToCartButton.isEnabled = true
        }
    }

    private fun updateToolbarAlpha(progress: Float) {
        // Update toolbar background alpha based on scroll progress
        val alpha = (progress * 255).toInt()
        binding.toolbar.background?.alpha = alpha
    }

    private fun loadProductDetails() {
        // Show shimmer while loading
        binding.scrollView.visibility = View.GONE
        
        lifecycleScope.launch {
            delay(1000) // Simulate network delay
            
            // Hide shimmer and show content with animation
            binding.scrollView.visibility = View.VISIBLE
            binding.scrollView.alpha = 0f
            binding.scrollView.animate()
                .alpha(1f)
                .setDuration(300)
                .start()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
