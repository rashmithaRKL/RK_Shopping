package com.example.rk_shop.ui.custom

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.core.content.ContextCompat
import com.example.rk_shop.R
import com.google.android.material.button.MaterialButton
import kotlin.math.min

class AnimatedActionButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = com.google.android.material.R.attr.materialButtonStyle
) : MaterialButton(context, attrs, defStyleAttr) {

    private var progress = 0f
    private var currentState = State.NORMAL
    private var originalText: CharSequence = ""
    
    private val progressPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = context.resources.getDimension(R.dimen.progress_stroke_width)
    }
    
    private val successPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = context.resources.getDimension(R.dimen.progress_stroke_width)
    }
    
    private val progressRect = RectF()
    private var centerX = 0f
    private var centerY = 0f
    private var radius = 0f

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.AnimatedActionButton,
            0, 0
        ).apply {
            try {
                progressPaint.color = getColor(
                    R.styleable.AnimatedActionButton_progressColor,
                    ContextCompat.getColor(context, R.color.primary)
                )
                successPaint.color = getColor(
                    R.styleable.AnimatedActionButton_successColor,
                    ContextCompat.getColor(context, R.color.success)
                )
            } finally {
                recycle()
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        centerX = w / 2f
        centerY = h / 2f
        radius = min(w, h) / 2f - progressPaint.strokeWidth
        progressRect.set(
            centerX - radius,
            centerY - radius,
            centerX + radius,
            centerY + radius
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        
        when (currentState) {
            State.LOADING -> {
                canvas.drawArc(
                    progressRect,
                    -90f,
                    progress * 360f,
                    false,
                    progressPaint
                )
            }
            State.SUCCESS -> {
                canvas.drawArc(
                    progressRect,
                    0f,
                    360f,
                    false,
                    successPaint
                )
            }
            else -> { /* No additional drawing needed */ }
        }
    }

    fun startAnimation() {
        if (currentState != State.NORMAL) return
        
        originalText = text
        currentState = State.LOADING
        
        // Animate to circular shape
        val widthAnimator = ValueAnimator.ofInt(width, height).apply {
            addUpdateListener { animator ->
                layoutParams.width = animator.animatedValue as Int
                requestLayout()
            }
        }

        // Animate progress
        val progressAnimator = ValueAnimator.ofFloat(0f, 1f).apply {
            addUpdateListener { animator ->
                progress = animator.animatedValue as Float
                invalidate()
            }
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.RESTART
        }

        AnimatorSet().apply {
            playTogether(
                widthAnimator,
                progressAnimator,
                ObjectAnimator.ofFloat(this@AnimatedActionButton, "alpha", 1f, 0.7f)
            )
            duration = 500
            interpolator = DecelerateInterpolator()
            start()
        }

        text = ""
        icon = null
    }

    fun success() {
        if (currentState != State.LOADING) return
        
        currentState = State.SUCCESS
        progress = 1f
        
        AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(this@AnimatedActionButton, "scaleX", 1f, 0.8f, 1.1f, 1f),
                ObjectAnimator.ofFloat(this@AnimatedActionButton, "scaleY", 1f, 0.8f, 1.1f, 1f),
                ObjectAnimator.ofFloat(this@AnimatedActionButton, "alpha", 0.7f, 1f)
            )
            duration = 500
            interpolator = AccelerateDecelerateInterpolator()
            start()
        }
    }

    fun reset() {
        if (currentState == State.NORMAL) return
        
        currentState = State.NORMAL
        progress = 0f
        
        // Animate back to original width
        val widthAnimator = ValueAnimator.ofInt(height, width).apply {
            addUpdateListener { animator ->
                layoutParams.width = animator.animatedValue as Int
                requestLayout()
            }
        }

        AnimatorSet().apply {
            play(widthAnimator)
            duration = 300
            interpolator = DecelerateInterpolator()
            start()
        }

        text = originalText
        icon = ContextCompat.getDrawable(context, R.drawable.ic_shopping_cart)
    }

    private enum class State {
        NORMAL, LOADING, SUCCESS
    }
}
