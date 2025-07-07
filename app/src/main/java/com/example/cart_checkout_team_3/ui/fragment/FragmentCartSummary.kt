package com.example.cart_checkout_team_3.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cart_checkout_team_3.R
import com.example.cart_checkout_team_3.data.models.Cart
import com.example.cart_checkout_team_3.data.models.CartItem
import com.example.cart_checkout_team_3.databinding.FragmentCartSummaryBinding
import com.example.cart_checkout_team_3.ui.adapter.SummaryAdapter
import com.example.cart_checkout_team_3.ui.viewmodel.CartViewModel

class FragmentCartSummary : Fragment() {

    private var _binding: FragmentCartSummaryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CartViewModel by activityViewModels()
    private lateinit var summaryAdapter: SummaryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartSummaryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupClickListeners()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        summaryAdapter = SummaryAdapter()

        binding.rvSummaryItems.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = summaryAdapter
            isNestedScrollingEnabled = false
            setHasFixedSize(true)
        }
    }

    private fun setupClickListeners() {
        binding.btnPlaceOrder.setOnClickListener {
            val cartItems = viewModel.cartItems.value
            if (cartItems.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "Your cart is empty", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
                return@setOnClickListener
            }

            viewModel.checkout()
        }
    }

    private fun observeViewModel() {
        viewModel.cartItems.observe(viewLifecycleOwner) { cartItems ->
            summaryAdapter.submitList(cartItems)
            updateSummary(cartItems)

            if (cartItems.isEmpty()) {
                Toast.makeText(requireContext(), "Cart is empty", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }
        }

        viewModel.cart.observe(viewLifecycleOwner) { cart ->
            cart?.let { updatePriceBreakdown(it) }
        }

        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            binding.btnPlaceOrder.isEnabled = !isLoading
            binding.btnPlaceOrder.text = if (isLoading) "Processing..." else "Place Order"
        }

        viewModel.checkoutResult.observe(viewLifecycleOwner) { result ->
            if (result.isNotEmpty()) {
                try {
                    val bundle = Bundle().apply {
                        putString("checkout_message", result)
                    }
                    findNavController().navigate(R.id.fragmentCheckoutResult, bundle)
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), result, Toast.LENGTH_LONG).show()
                    findNavController().navigateUp()
                }
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
                viewModel.clearError()
            }
        }
    }

    private fun updateSummary(cartItems: List<CartItem>) {
        val itemCount = cartItems.sumOf { it.quantity }
        val subtotal = cartItems.sumOf { it.total }

        // Show summary in toast since UI elements might not exist
        Toast.makeText(
            requireContext(),
            "$itemCount items - Subtotal: $${String.format("%.2f", subtotal)}",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun updatePriceBreakdown(cart: Cart) {
        val discount = cart.total - cart.discountedTotal
        val tax = cart.discountedTotal * 0.08
        val shipping = if (cart.discountedTotal > 50) 0.0 else 5.99
        val finalTotal = cart.discountedTotal + tax + shipping

        // Show price breakdown in toast
        Toast.makeText(
            requireContext(),
            "Total: $${String.format("%.2f", finalTotal)} (includes tax & shipping)",
            Toast.LENGTH_LONG
        ).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
