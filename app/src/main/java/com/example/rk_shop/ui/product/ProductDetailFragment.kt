package com.example.rk_shop.ui.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.rk_shop.databinding.ActivityProductDetailBinding
import com.example.rk_shop.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductDetailFragment : BaseFragment<ActivityProductDetailBinding>() {

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): ActivityProductDetailBinding = ActivityProductDetailBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupViews()
    }

    private fun setupViews() {
        binding.apply {
            // Setup add to cart button
            btnAddToCart.setOnClickListener {
                // Add to cart logic
                findNavController().navigateUp()
            }

            // Setup add to wishlist button
            btnAddToWishlist.setOnClickListener {
                // Add to wishlist logic
            }

            // Setup back button
            btnBack.setOnClickListener {
                findNavController().navigateUp()
            }

            // Setup share button
            btnShare.setOnClickListener {
                // Share product logic
            }
        }
    }
}
