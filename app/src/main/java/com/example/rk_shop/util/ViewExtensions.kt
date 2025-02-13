package com.example.rk_shop.util

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AnticipateOvershootInterpolator
import androidx.core.view.isVisible
import androidx.transition.Fade
import androidx.transition.TransitionManager

fun View.fadeIn(duration: Long = 300) {
    alpha = 0f
    isVisible = true
    animate()
        .alpha(1f)
        .setDuration(duration)
        .setInterpolator(AccelerateDecelerateInterpolator())
        .start()
}

fun View.fadeOut(duration: Long = 300, gone: Boolean = false) {
    animate()
        .alpha(0f)
        .setDuration(duration)
        .setInterpolator(AccelerateDecelerateInterpolator())
        .withEndAction { 
            visibility = if (gone) View.GONE else View.INVISIBLE
        }
        .start()
}

fun View.slideUp(duration: Long = 300) {
    translationY = height.toFloat()
    isVisible = true
    animate()
        .translationY(0f)
        .setDuration(duration)
        .setInterpolator(AccelerateDecelerateInterpolator())
        .start()
}

fun View.slideDown(duration: Long = 300) {
    animate()
        .translationY(height.toFloat())
        .setDuration(duration)
        .setInterpolator(AccelerateDecelerateInterpolator())
        .withEndAction { isVisible = false }
        .start()
}

fun View.scaleIn(duration: Long = 300) {
    scaleX = 0f
    scaleY = 0f
    isVisible = true
    
    val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 0f, 1f)
    val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 0f, 1f)
    
    ObjectAnimator.ofPropertyValuesHolder(this, scaleX, scaleY).apply {
        this.duration = duration
        interpolator = AnticipateOvershootInterpolator()
        start()
    }
}

fun View.scaleOut(duration: Long = 300) {
    val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1f, 0f)
    val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, 0f)
    
    ObjectAnimator.ofPropertyValuesHolder(this, scaleX, scaleY).apply {
        this.duration = duration
        interpolator = AccelerateDecelerateInterpolator()
        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                isVisible = false
            }
        })
        start()
    }
}

fun View.shake() {
    val animator = ObjectAnimator.ofFloat(this, View.TRANSLATION_X, 0f, 25f, -25f, 25f, -25f, 15f, -15f, 6f, -6f, 0f)
    animator.duration = 500
    animator.interpolator = AccelerateDecelerateInterpolator()
    animator.start()
}

fun View.bounce() {
    val animator = ObjectAnimator.ofFloat(this, View.TRANSLATION_Y, 0f, -30f, 0f)
    animator.duration = 500
    animator.interpolator = AnticipateOvershootInterpolator()
    animator.start()
}

fun ViewGroup.fadeTransition(duration: Long = 300) {
    TransitionManager.beginDelayedTransition(this, Fade().apply {
        this.duration = duration
    })
}

fun View.rotateAnimation(degree: Float, duration: Long = 300) {
    ObjectAnimator.ofFloat(this, View.ROTATION, rotation, degree).apply {
        this.duration = duration
        interpolator = AccelerateDecelerateInterpolator()
        start()
    }
}

fun View.pulseAnimation() {
    val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1f, 1.2f, 1f)
    val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, 1.2f, 1f)
    
    ObjectAnimator.ofPropertyValuesHolder(this, scaleX, scaleY).apply {
        duration = 300
        interpolator = AccelerateDecelerateInterpolator()
        start()
    }
}

fun View.pressAnimation() {
    animate()
        .scaleX(0.95f)
        .scaleY(0.95f)
        .setDuration(100)
        .withEndAction {
            animate()
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(100)
                .start()
        }
        .start()
}
