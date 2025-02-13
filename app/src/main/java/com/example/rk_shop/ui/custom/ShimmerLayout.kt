package com.example.rk_shop.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.view.isVisible
import com.facebook.shimmer.ShimmerFrameLayout
import com.example.rk_shop.R

class ShimmerLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val shimmerFrameLayout: ShimmerFrameLayout

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_shimmer, this, true)
        shimmerFrameLayout = view.findViewById(R.id.shimmerFrameLayout)
    }

    fun startShimmer() {
        isVisible = true
        shimmerFrameLayout.startShimmer()
    }

    fun stopShimmer() {
        shimmerFrameLayout.stopShimmer()
        isVisible = false
    }

    override fun onDetachedFromWindow() {
        shimmerFrameLayout.stopShimmer()
        super.onDetachedFromWindow()
    }
}
