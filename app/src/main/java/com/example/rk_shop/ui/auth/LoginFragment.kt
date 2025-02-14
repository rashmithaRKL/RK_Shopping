package com.example.rk_shop.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.rk_shop.R
import com.example.rk_shop.databinding.ActivityLoginBinding
import com.example.rk_shop.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment<ActivityLoginBinding>() {

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): ActivityLoginBinding = ActivityLoginBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupViews()
    }

    private fun setupViews() {
        // Setup login button
        binding.btnLogin.setOnClickListener {
            // Perform login
            // On successful login:
            findNavController().navigate(R.id.action_login_to_home)
        }

        // Setup signup navigation
        binding.btnSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_signup)
        }
    }
}
