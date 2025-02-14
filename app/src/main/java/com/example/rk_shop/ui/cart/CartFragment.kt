package com.example.rk_shop.ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.rk_shop.databinding.ActivityCartBinding
import com.example.rk_shop.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartFragment : BaseFragment<ActivityCartBinding>() {

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): ActivityCartBinding = ActivityCartBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupViews()
    }

    private fun setupViews() {
        binding.apply {
            // Setup checkout button
            btnCheckout.setOnClickListener {
                // Navigate to payment/checkout flow
            }

            // Setup continue shopping button
            btnContinueShopping.setOnClickListener {
                findNavController().navigateUp()
            }

            // Setup clear cart button
            btnClearCart.setOnClickListener {
                // Clear cart logic
            }

            // Setup back button
            btnBack.setOnClickListener {
                findNavController().navigateUp()
            }
        }
    }
}
