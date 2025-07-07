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
import com.example.cart_checkout_team_3.data.models.CartItem
import com.example.cart_checkout_team_3.databinding.FragmentCartListBinding
import com.example.cart_checkout_team_3.ui.adapter.CartAdapter
import com.example.cart_checkout_team_3.ui.viewmodel.CartViewModel

class FragmentCartList : Fragment() {

    private var _binding: FragmentCartListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CartViewModel by activityViewModels()
    private lateinit var cartAdapter: CartAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupClickListeners()
        observeViewModel()

        // Load cart data for current user when fragment becomes visible
        viewModel.user.value?.let { user ->
            viewModel.loadCartForUser(user.id)
        }
    }

    override fun onResume() {
        super.onResume()
        // Refresh cart data when returning to this fragment
        viewModel.user.value?.let { user ->
            viewModel.loadCartForUser(user.id)
        }
    }

    private fun setupRecyclerView() {
        cartAdapter = CartAdapter(
            onQuantityChanged = { productId, newQuantity ->
                if (newQuantity > 0) {
                    viewModel.updateCartItemQuantity(productId, newQuantity)
                } else {
                    viewModel.removeFromCart(productId)
                }
            },
            onRemoveItem = { productId ->
                viewModel.removeFromCart(productId)
            },
            onItemClick = { cartItem ->
                // Navigate to product detail or quantity selector for editing
                try {
                    val bundle = Bundle().apply {
                        putParcelable("cart_item", cartItem)
                    }
                    findNavController().navigate(R.id.fragmentQuantitySelector, bundle)
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "Navigation error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        )

        binding.rvCartItems.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = cartAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupClickListeners() {
        binding.btnCheckout.setOnClickListener {
            val cartItems = viewModel.cartItems.value
            if (cartItems.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "Cart is empty. Add some products first.", Toast.LENGTH_SHORT).show()
            } else {
                try {
                    findNavController().navigate(R.id.fragmentCartSummary)
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "Navigation error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.btnStartShopping.setOnClickListener {
            try {
                findNavController().navigate(R.id.fragmentProductPicker)
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Navigation error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeViewModel() {
        // Observe cart items
        viewModel.cartItems.observe(viewLifecycleOwner) { cartItems ->
            cartAdapter.submitList(cartItems)

            if (cartItems.isEmpty()) {
                showEmptyCart()
            } else {
                showCartItems()
                updateCartSummary(cartItems)
            }
        }

        // Observe cart for totals
        viewModel.cart.observe(viewLifecycleOwner) { cart ->
            cart?.let {
                updateCartTotals(it.total, it.discountedTotal)
            }
        }

        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.btnCheckout.isEnabled = !isLoading
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
                viewModel.clearError()
            }
        }
    }

    private fun showEmptyCart() {
        binding.apply {
            rvCartItems.visibility = View.GONE
            layoutEmptyCart.visibility = View.VISIBLE
            btnCheckout.isEnabled = false
            // Update header to show 0 items when cart is empty
            tvCartItemCount.text = "0 items in cart"
        }
    }

    private fun showCartItems() {
        binding.apply {
            rvCartItems.visibility = View.VISIBLE
            layoutEmptyCart.visibility = View.GONE
            btnCheckout.isEnabled = true
        }
    }

    private fun updateCartSummary(cartItems: List<CartItem>) {
        val itemCount = cartItems.size
        val totalQuantity = cartItems.sumOf { it.quantity }

        binding.tvCartItemCount.text = if (itemCount == 0) {
            "0 items in cart"
        } else {
            "$itemCount item${if (itemCount > 1) "s" else ""} ($totalQuantity piece${if (totalQuantity > 1) "s" else ""}) in cart"
        }
    }

    private fun updateCartTotals(total: Double, discountedTotal: Double) {
        binding.tvCartTotal.text = "Total: $${String.format("%.2f", discountedTotal)}"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
