package com.example.rk_shop.ui.product

import android.os.Bundle
import android.view.View
import androidx.core.graphics.Insets
import androidx.core.view.doOnPreDraw
import androidx.core.view.updatePadding
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.example.rk_shop.databinding.ActivityProductDetailBinding
import com.example.rk_shop.ui.base.BaseActivity
import com.example.rk_shop.ui.custom.CustomTabLayoutMediator
import com.example.rk_shop.ui.custom.PageTransformer
import com.example.rk_shop.util.pressAnimation
import com.example.rk_shop.util.pulseAnimation
import com.google.android.material.motion.MotionUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ProductDetailActivity : BaseActivity<ActivityProductDetailBinding>() {

    private lateinit var imageAdapter: ProductImageAdapter
    private var isFavorite = false

    override fun getViewBinding(): ActivityProductDetailBinding {
        return ActivityProductDetailBinding.inflate(layoutInflater)
    }

    override fun setupViews(savedInstanceState: Bundle?) {
        setupToolbar()
        setupImagePager()
        setupClickListeners()
        loadProductDetails()
    }

    override fun handleWindowInsets(insets: Insets) {
        // Apply insets to toolbar
        binding.toolbar.updatePadding(top = insets.top)
        
        // Apply insets to bottom bar
        binding.bottomBar.updatePadding(bottom = insets.bottom)
        
        // Update MotionLayout constraints for the content
        binding.motionLayout.getConstraintSet(com.example.rk_shop.R.id.start)?.let { constraintSet ->
            constraintSet.setMargin(
                com.example.rk_shop.R.id.scrollView,
                androidx.constraintlayout.widget.ConstraintSet.TOP,
                insets.top
            )
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    private fun setupImagePager() {
        imageAdapter = ProductImageAdapter().apply {
            setOnImageClickListener { position ->
                // Handle image click, maybe show full-screen image
                binding.imageViewPager.currentItem = position
            }
        }

        binding.imageViewPager.apply {
            adapter = imageAdapter
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            setPageTransformer(PageTransformer())

            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    binding.imageIndicator.selectTab(binding.imageIndicator.getTabAt(position))
                }
            })
        }

        // Setup custom tab mediator
        CustomTabLayoutMediator(
            binding.imageIndicator,
            binding.imageViewPager,
            true,
            true
        ) { tab, position ->
            // Configure tab appearance if needed
        }.attach()
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
            handleBuyNow()
        }

        // Setup motion layout transition listener
        binding.motionLayout.setTransitionListener(object : androidx.constraintlayout.motion.widget.MotionLayout.TransitionListener {
            override fun onTransitionStarted(layout: androidx.constraintlayout.motion.widget.MotionLayout?, startId: Int, endId: Int) {}
            
            override fun onTransitionChange(layout: androidx.constraintlayout.motion.widget.MotionLayout?, startId: Int, endId: Int, progress: Float) {
                updateToolbarAlpha(progress)
            }
            
            override fun onTransitionCompleted(layout: androidx.constraintlayout.motion.widget.MotionLayout?, currentId: Int) {}
            
            override fun onTransitionTrigger(layout: androidx.constraintlayout.motion.widget.MotionLayout?, triggerId: Int, positive: Boolean, progress: Float) {}
        })
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
            // Start loading animation
            binding.addToCartButton.startAnimation()
            
            delay(1500) // Simulate network call
            
            // Show success animation
            binding.addToCartButton.success()
            
            delay(1000)
            
            // Reset button state
            binding.addToCartButton.reset()
            binding.addToCartButton.isEnabled = true
        }
    }

    private fun handleBuyNow() {
        // Animate button and navigate to checkout
        binding.buyNowButton.pulseAnimation()
        
        lifecycleScope.launch {
            delay(200)
            // Navigate to checkout
        }
    }

    private fun loadProductDetails() {
        // Show shimmer while loading
        binding.shimmerLayout.startShimmer()
        binding.contentLayout.visibility = View.INVISIBLE
        
        lifecycleScope.launch {
            delay(1500) // Simulate network delay
            
            // Load product images
            val images = listOf(
                "https://example.com/image1.jpg",
                "https://example.com/image2.jpg",
                "https://example.com/image3.jpg"
            )
            imageAdapter.setImages(images)
            
            // Hide shimmer and show content with animation
            binding.shimmerLayout.stopShimmer()
            binding.shimmerLayout.visibility = View.GONE
            binding.contentLayout.apply {
                alpha = 0f
                visibility = View.VISIBLE
                animate()
                    .alpha(1f)
                    .setDuration(300)
                    .setInterpolator(MotionUtils.resolveThemeInterpolator(
                        context,
                        com.google.android.material.R.attr.motionEasingEmphasizedInterpolator,
                        android.view.animation.AccelerateDecelerateInterpolator()
                    ))
                    .start()
            }
        }
    }

    private fun updateToolbarAlpha(progress: Float) {
        val alpha = (progress * 255).toInt()
        binding.toolbar.background?.alpha = alpha
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
