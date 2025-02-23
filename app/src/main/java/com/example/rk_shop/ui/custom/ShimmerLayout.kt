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

        // Apply custom attributes if provided
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(
                attrs,
                R.styleable.ShimmerLayout,
                defStyleAttr,
                0
            )

            try {
                // Apply custom shimmer attributes
                val shimmerColor = typedArray.getColor(
                    R.styleable.ShimmerLayout_shimmerColor,
                    shimmerFrameLayout.highlightColor
                )
                val shimmerDuration = typedArray.getInteger(
                    R.styleable.ShimmerLayout_shimmerDuration,
                    shimmerFrameLayout.duration.toInt()
                )
                val shimmerAngle = typedArray.getInteger(
                    R.styleable.ShimmerLayout_shimmerAngle,
                    shimmerFrameLayout.angle
                )

                // Apply the attributes to the ShimmerFrameLayout
                shimmerFrameLayout.setHighlightColor(shimmerColor)
                shimmerFrameLayout.duration = shimmerDuration.toLong()
                shimmerFrameLayout.angle = shimmerAngle
            } finally {
                typedArray.recycle()
            }
        }
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

    // Expose shimmer customization methods
    fun setShimmerColor(color: Int) {
        shimmerFrameLayout.setHighlightColor(color)
    }

    fun setShimmerDuration(duration: Long) {
        shimmerFrameLayout.duration = duration
    }

    fun setShimmerAngle(angle: Int) {
        shimmerFrameLayout.angle = angle
    }

    fun isShimmerStarted(): Boolean {
        return shimmerFrameLayout.isShimmerStarted
    }
}
