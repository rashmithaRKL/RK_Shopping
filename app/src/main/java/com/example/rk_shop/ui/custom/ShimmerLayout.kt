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
        // Inflate the layout
        val view = LayoutInflater.from(context).inflate(R.layout.layout_shimmer, this, true)
        shimmerFrameLayout = view.findViewById(R.id.shimmerFrameLayout)

        // Apply custom attributes if provided
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(
                attrs,
                R.styleable.ShimmerLayout,
                defStyleAttr,
                0
            )

            try {
                // Get custom attributes
                val shimmerColor = typedArray.getColor(
                    R.styleable.ShimmerLayout_shimmerColor,
                    context.getColor(android.R.color.white)
                )
                val shimmerDuration = typedArray.getInteger(
                    R.styleable.ShimmerLayout_shimmerDuration,
                    1000
                )
                val shimmerAngle = typedArray.getInteger(
                    R.styleable.ShimmerLayout_shimmerAngle,
                    0
                )

                // Apply attributes to ShimmerFrameLayout
                with(shimmerFrameLayout) {
                    setShimmerColor(shimmerColor)
                    duration = shimmerDuration.toLong()
                    angle = shimmerAngle
                    setShimmerAnimationDuration(shimmerDuration.toLong())
                }
            } finally {
                typedArray.recycle()
            }
        }
    }

    /**
     * Start the shimmer animation
     */
    fun startShimmer() {
        isVisible = true
        shimmerFrameLayout.startShimmer()
    }

    /**
     * Stop the shimmer animation
     */
    fun stopShimmer() {
        shimmerFrameLayout.stopShimmer()
        isVisible = false
    }

    override fun onDetachedFromWindow() {
        shimmerFrameLayout.stopShimmer()
        super.onDetachedFromWindow()
    }

    /**
     * Set the shimmer color
     */
    fun setShimmerColor(color: Int) {
        shimmerFrameLayout.setShimmerColor(color)
    }

    /**
     * Set the shimmer animation duration
     */
    fun setShimmerDuration(duration: Long) {
        shimmerFrameLayout.duration = duration
    }

    /**
     * Set the shimmer angle
     */
    fun setShimmerAngle(angle: Int) {
        shimmerFrameLayout.angle = angle
    }

    /**
     * Check if shimmer animation is currently running
     */
    fun isShimmerStarted(): Boolean {
        return shimmerFrameLayout.isShimmerStarted
    }
}
