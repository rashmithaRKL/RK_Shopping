package com.example.rk_shop.util

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AnimationUtils
import android.view.animation.OvershootInterpolator
import androidx.core.view.isVisible

object AnimationUtils {
    
    fun fadeIn(view: View, duration: Long = 300) {
        view.alpha = 0f
        view.isVisible = true
        view.animate()
            .alpha(1f)
            .setDuration(duration)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .start()
    }

    fun fadeOut(view: View, duration: Long = 300) {
        view.animate()
            .alpha(0f)
            .setDuration(duration)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .withEndAction { view.isVisible = false }
            .start()
    }

    fun scaleIn(view: View, duration: Long = 300) {
        view.scaleX = 0f
        view.scaleY = 0f
        view.isVisible = true
        
        val scaleX = ObjectAnimator.ofFloat(view, View.SCALE_X, 0f, 1f)
        val scaleY = ObjectAnimator.ofFloat(view, View.SCALE_Y, 0f, 1f)
        
        AnimatorSet().apply {
            playTogether(scaleX, scaleY)
            interpolator = OvershootInterpolator()
            this.duration = duration
            start()
        }
    }

    fun slideInFromBottom(view: View, duration: Long = 300) {
        view.translationY = view.height.toFloat()
        view.isVisible = true
        view.animate()
            .translationY(0f)
            .setDuration(duration)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .start()
    }

    fun slideOutToBottom(view: View, duration: Long = 300) {
        view.animate()
            .translationY(view.height.toFloat())
            .setDuration(duration)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .withEndAction { view.isVisible = false }
            .start()
    }

    fun pulseAnimation(view: View) {
        val scaleX = ObjectAnimator.ofFloat(view, View.SCALE_X, 1f, 1.2f, 1f)
        val scaleY = ObjectAnimator.ofFloat(view, View.SCALE_Y, 1f, 1.2f, 1f)
        
        AnimatorSet().apply {
            playTogether(scaleX, scaleY)
            duration = 300
            interpolator = AccelerateDecelerateInterpolator()
            start()
        }
    }

    fun shakeAnimation(view: View) {
        val animation = android.view.animation.TranslateAnimation(-10f, 10f, 0f, 0f).apply {
            duration = 100
            repeatMode = android.view.animation.Animation.REVERSE
            repeatCount = 3
        }
        view.startAnimation(animation)
    }

    fun bounceAnimation(view: View) {
        val animator = ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, 0f, -30f, 0f)
        animator.interpolator = OvershootInterpolator()
        animator.duration = 500
        animator.start()
    }
}
