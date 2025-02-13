package com.example.rk_shop.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.example.rk_shop.R
import com.example.rk_shop.databinding.ViewQuantitySelectorBinding
import com.example.rk_shop.util.pressAnimation
import com.example.rk_shop.util.shakeAnimation

class QuantitySelector @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding: ViewQuantitySelectorBinding
    private var quantity = 1
    private var maxQuantity = Int.MAX_VALUE
    private var minQuantity = 1
    private var onQuantityChangeListener: ((Int) -> Unit)? = null

    init {
        binding = ViewQuantitySelectorBinding.inflate(LayoutInflater.from(context), this, true)
        setupViews()
        setupListeners()
        updateQuantityText()
        updateButtonStates()
    }

    private fun setupViews() {
        context.theme.obtainStyledAttributes(
            R.styleable.QuantitySelector,
            intArrayOf(
                R.attr.quantitySelectorBackground,
                R.attr.quantitySelectorTextColor,
                R.attr.quantitySelectorButtonTint
            ),
            0, 0
        ).apply {
            try {
                binding.root.background = getDrawable(0)
                binding.quantityText.setTextColor(
                    getColor(
                        1,
                        ContextCompat.getColor(context, R.color.text_primary)
                    )
                )
                val buttonTint = getColor(
                    2,
                    ContextCompat.getColor(context, R.color.primary)
                )
                binding.decreaseButton.setColorFilter(buttonTint)
                binding.increaseButton.setColorFilter(buttonTint)
            } finally {
                recycle()
            }
        }
    }

    private fun setupListeners() {
        binding.decreaseButton.setOnClickListener {
            if (quantity > minQuantity) {
                it.pressAnimation()
                quantity--
                updateQuantityText()
                updateButtonStates()
                onQuantityChangeListener?.invoke(quantity)
            } else {
                it.shakeAnimation()
            }
        }

        binding.increaseButton.setOnClickListener {
            if (quantity < maxQuantity) {
                it.pressAnimation()
                quantity++
                updateQuantityText()
                updateButtonStates()
                onQuantityChangeListener?.invoke(quantity)
            } else {
                it.shakeAnimation()
            }
        }
    }

    private fun updateQuantityText() {
        binding.quantityText.text = quantity.toString()
    }

    private fun updateButtonStates() {
        binding.decreaseButton.isEnabled = quantity > minQuantity
        binding.increaseButton.isEnabled = quantity < maxQuantity
        
        binding.decreaseButton.alpha = if (quantity > minQuantity) 1f else 0.5f
        binding.increaseButton.alpha = if (quantity < maxQuantity) 1f else 0.5f
    }

    fun setQuantity(value: Int) {
        quantity = value.coerceIn(minQuantity, maxQuantity)
        updateQuantityText()
        updateButtonStates()
    }

    fun getQuantity(): Int = quantity

    fun setMaxQuantity(value: Int) {
        maxQuantity = value
        quantity = quantity.coerceAtMost(maxQuantity)
        updateQuantityText()
        updateButtonStates()
    }

    fun setMinQuantity(value: Int) {
        minQuantity = value
        quantity = quantity.coerceAtLeast(minQuantity)
        updateQuantityText()
        updateButtonStates()
    }

    fun setOnQuantityChangeListener(listener: (Int) -> Unit) {
        onQuantityChangeListener = listener
    }
}
