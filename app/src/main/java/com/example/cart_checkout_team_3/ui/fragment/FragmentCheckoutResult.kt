package com.example.cart_checkout_team_3.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.cart_checkout_team_3.R
import com.example.cart_checkout_team_3.databinding.FragmentCheckoutResultBinding
import com.example.cart_checkout_team_3.ui.viewmodel.CartViewModel

class FragmentCheckoutResult : Fragment() {

    private var _binding: FragmentCheckoutResultBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CartViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCheckoutResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get checkout message from arguments
        val checkoutMessage = arguments?.getString("checkout_message") ?: "Checkout completed successfully!"

        setupUI(checkoutMessage)
        setupClickListeners()
    }

    private fun setupUI(message: String) {
        binding.tvCheckoutMessage.text = message
    }

    private fun setupClickListeners() {
        binding.btnContinueShopping.setOnClickListener {
            try {
                // Navigate back to product picker
                findNavController().navigate(R.id.fragmentProductPicker)
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Navigation error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnViewOrders.setOnClickListener {
            try {
                // Navigate to cart history
                findNavController().navigate(R.id.fragmentCartHistory)
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Navigation error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
