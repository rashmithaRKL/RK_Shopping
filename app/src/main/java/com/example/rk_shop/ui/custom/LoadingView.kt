package com.example.rk_shop.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.view.isVisible
import com.airbnb.lottie.LottieAnimationView
import com.example.rk_shop.R

class LoadingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val lottieView: LottieAnimationView

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.view_loading, this, true)
        lottieView = view.findViewById(R.id.lottieAnimationView)
    }

    fun startLoading() {
        isVisible = true
        lottieView.playAnimation()
    }

    fun stopLoading() {
        lottieView.cancelAnimation()
        isVisible = false
    }
}
