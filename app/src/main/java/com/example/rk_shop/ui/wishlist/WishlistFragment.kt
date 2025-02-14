package com.example.rk_shop.ui.wishlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.rk_shop.R
import com.example.rk_shop.databinding.ActivityWishlistBinding
import com.example.rk_shop.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WishlistFragment : BaseFragment<ActivityWishlistBinding>() {

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): ActivityWishlistBinding = ActivityWishlistBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupViews()
    }

    private fun setupViews() {
        binding.apply {
            // Setup move all to cart button
            btnMoveAllToCart.setOnClickListener {
                // Move all items to cart
                findNavController().navigate(R.id.action_wishlist_to_cart)
            }

            // Setup clear wishlist button
            btnClearWishlist.setOnClickListener {
                // Clear wishlist logic
            }

            // Setup back button
            btnBack.setOnClickListener {
                findNavController().navigateUp()
            }

            // Setup continue shopping button
            btnContinueShopping.setOnClickListener {
                findNavController().navigateUp()
            }
        }
    }
}
