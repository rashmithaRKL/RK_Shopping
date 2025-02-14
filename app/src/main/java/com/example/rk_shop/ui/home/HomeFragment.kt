package com.example.rk_shop.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.rk_shop.R
import com.example.rk_shop.databinding.ActivityHomeBinding
import com.example.rk_shop.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<ActivityHomeBinding>() {

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): ActivityHomeBinding = ActivityHomeBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupViews()
    }

    private fun setupViews() {
        // Setup navigation to other screens
        binding.apply {
            // Navigate to cart
            btnCart.setOnClickListener {
                findNavController().navigate(R.id.action_home_to_cart)
            }

            // Navigate to wishlist
            btnWishlist.setOnClickListener {
                findNavController().navigate(R.id.action_home_to_wishlist)
            }

            // Navigate to product details (example with sample product id)
            btnProductDetails.setOnClickListener {
                findNavController().navigate(R.id.action_home_to_product)
            }

            // Setup other click listeners for navigation
            btnSearch.setOnClickListener {
                findNavController().navigate(R.id.searchFragment)
            }

            btnProfile.setOnClickListener {
                findNavController().navigate(R.id.profileFragment)
            }

            btnCategories.setOnClickListener {
                findNavController().navigate(R.id.categoryFragment)
            }
        }
    }
}
