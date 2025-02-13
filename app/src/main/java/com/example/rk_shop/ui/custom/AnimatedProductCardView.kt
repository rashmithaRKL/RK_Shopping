package com.example.rk_shop.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import com.example.rk_shop.R
import com.example.rk_shop.util.pressAnimation
import com.example.rk_shop.util.scaleIn
import com.google.android.material.card.MaterialCardView
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton

class AnimatedProductCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = com.google.android.material.R.attr.materialCardViewStyle
) : MaterialCardView(context, attrs, defStyleAttr) {

    private var productImage: ImageView
    private var productTitle: TextView
    private var productPrice: TextView
    private var productRating: TextView
    private var addToCartButton: MaterialButton
    private var favoriteButton: MaterialButton
    private var onProductClickListener: (() -> Unit)? = null
    private var onAddToCartClickListener: (() -> Unit)? = null
    private var onFavoriteClickListener: (() -> Unit)? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.view_animated_product_card, this, true)
        
        productImage = findViewById(R.id.productImage)
        productTitle = findViewById(R.id.productTitle)
        productPrice = findViewById(R.id.productPrice)
        productRating = findViewById(R.id.productRating)
        addToCartButton = findViewById(R.id.addToCartButton)
        favoriteButton = findViewById(R.id.favoriteButton)

        setupClickListeners()
        setupTouchAnimation()
    }

    private fun setupClickListeners() {
        setOnClickListener {
            onProductClickListener?.invoke()
        }

        addToCartButton.setOnClickListener {
            it.pressAnimation()
            onAddToCartClickListener?.invoke()
        }

        favoriteButton.setOnClickListener {
            it.pressAnimation()
            onFavoriteClickListener?.invoke()
        }
    }

    private fun setupTouchAnimation() {
        setOnTouchListener { view, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    view.pressAnimation()
                    false
                }
                else -> false
            }
        }
    }

    fun setProduct(
        imageUrl: String,
        title: String,
        price: String,
        rating: Float,
        isFavorite: Boolean = false
    ) {
        Glide.with(context)
            .load(imageUrl)
            .into(productImage)

        productTitle.text = title
        productPrice.text = price
        productRating.text = rating.toString()
        favoriteButton.isSelected = isFavorite

        // Animate the card when data is set
        scaleIn(300)
    }

    fun setOnProductClickListener(listener: () -> Unit) {
        onProductClickListener = listener
    }

    fun setOnAddToCartClickListener(listener: () -> Unit) {
        onAddToCartClickListener = listener
    }

    fun setOnFavoriteClickListener(listener: () -> Unit) {
        onFavoriteClickListener = listener
    }

    fun showAddToCartAnimation() {
        addToCartButton.isEnabled = false
        // Add success animation here
        postDelayed({
            addToCartButton.isEnabled = true
        }, 1000)
    }

    fun toggleFavorite(isFavorite: Boolean) {
        favoriteButton.isSelected = isFavorite
        favoriteButton.pressAnimation()
    }
}
