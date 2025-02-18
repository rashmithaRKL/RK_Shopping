package com.example.rk_shop.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.rk_shop.R
import com.example.rk_shop.databinding.ActivitySignUpBinding
import com.example.rk_shop.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : BaseFragment<ActivitySignUpBinding>() {

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): ActivitySignUpBinding = ActivitySignUpBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupViews()
    }

    private fun setupViews() {
        // Setup signup button
        binding.btnSignUp.setOnClickListener {
        // Perform signup
        val email = binding.editTextEmail.text.toString()
        val password = binding.editTextPassword.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(requireContext(), "Email and Password cannot be empty", Toast.LENGTH_SHORT).show()
            return@setOnClickListener
        }

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
        // On successful signup, navigate to home
                } else {
                    Toast.makeText(requireContext(), "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
            // On successful signup:
            findNavController().navigate(R.id.action_signup_to_home)
        }

        // Setup back to login
        binding.btnBackToLogin.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}
