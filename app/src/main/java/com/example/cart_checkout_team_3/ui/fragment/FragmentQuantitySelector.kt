package com.example.cart_checkout_team_3.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.cart_checkout_team_3.R
import com.example.cart_checkout_team_3.data.models.Product
import com.example.cart_checkout_team_3.data.models.CartItem
import com.example.cart_checkout_team_3.databinding.FragmentQuantitySelectorBinding
import com.example.cart_checkout_team_3.ui.viewmodel.CartViewModel

class FragmentQuantitySelector : Fragment() {

    private var _binding: FragmentQuantitySelectorBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CartViewModel by activityViewModels()
    private var product: Product? = null
    private var cartItem: CartItem? = null
    private var quantity = 1
    private var isEditMode = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuantitySelectorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Check if we're editing existing cart item or adding new product
        cartItem = arguments?.getParcelable("cart_item")
        product = arguments?.getParcelable("product")

        if (cartItem != null) {
            // Edit existing cart item
            isEditMode = true
            quantity = cartItem!!.quantity
            setupCartItemDetails(cartItem!!)
        } else if (product != null) {
            // Add new product to cart
            isEditMode = false
            setupProductDetails(product!!)
        }

        setupClickListeners()
        observeViewModel()
        updateQuantityDisplay()
        updateTotalPrice()
    }

    private fun setupCartItemDetails(item: CartItem) {
        binding.apply {
            tvProductTitle.text = item.title
            tvProductPrice.text = "$${String.format("%.2f", item.price)}"
            btnUpdateCart.text = "Update Cart"

            // Load product image with fallback
            Glide.with(requireContext())
                .load(item.thumbnail)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_gallery)
                .into(ivProductImage)
        }
    }

    private fun setupProductDetails(product: Product) {
        binding.apply {
            tvProductTitle.text = product.title
            tvProductPrice.text = "$${String.format("%.2f", product.price)}"
            btnUpdateCart.text = "Add to Cart"

            // Load product image with fallback
            Glide.with(requireContext())
                .load(product.thumbnail)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_gallery)
                .into(ivProductImage)
        }
    }

    private fun setupClickListeners() {
        binding.apply {
            btnIncrease.setOnClickListener {
                val maxStock = product?.stock ?: 99
                if (quantity < maxStock) {
                    quantity++
                    updateQuantityDisplay()
                    updateTotalPrice()
                } else {
                    Toast.makeText(requireContext(), "Maximum stock reached", Toast.LENGTH_SHORT).show()
                }
            }

            btnDecrease.setOnClickListener {
                if (quantity > 1) {
                    quantity--
                    updateQuantityDisplay()
                    updateTotalPrice()
                }
            }

            btnCancel.setOnClickListener {
                // Navigate back without making changes
                try {
                    findNavController().navigateUp()
                } catch (e: Exception) {
                    // Fallback navigation
                    activity?.onBackPressed()
                }
            }

            btnUpdateCart.setOnClickListener {
                if (isEditMode && cartItem != null) {
                    // Update existing cart item
                    viewModel.updateCartItemQuantity(cartItem!!.id, quantity)
                    Toast.makeText(requireContext(), "Cart updated successfully", Toast.LENGTH_SHORT).show()
                } else if (product != null) {
                    // Add new product to cart
                    viewModel.addToCart(product!!.id, quantity)

                    // Immediately navigate back to product list after adding
                    Toast.makeText(requireContext(), "Added to cart successfully", Toast.LENGTH_SHORT).show()
                    navigateToProductList()
                }
            }
        }
    }

    private fun observeViewModel() {
        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            binding.btnUpdateCart.isEnabled = !isLoading
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
                viewModel.clearError()
            }
        }

        // Simplified and more reliable navigation logic
        var hasNavigated = false

        viewModel.cartItems.observe(viewLifecycleOwner) { cartItems ->
            if (!viewModel.loading.value!! && !hasNavigated) {
                if (!isEditMode && product != null) {
                    // For add mode: check if item was actually added
                    val addedItem = cartItems.find { it.id == product!!.id }
                    if (addedItem != null) {
                        hasNavigated = true
                        Toast.makeText(requireContext(), "Added to cart successfully", Toast.LENGTH_SHORT).show()

                        // Navigate back to product list immediately after successful add
                        navigateToProductList()
                    }
                } else if (isEditMode && cartItem != null) {
                    // For edit mode: just show success message
                    Toast.makeText(requireContext(), "Cart updated successfully", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun navigateToProductList() {
        try {
            // Try to navigate to product picker directly
            findNavController().navigate(R.id.fragmentProductPicker)
        } catch (e: Exception) {
            try {
                // Fallback 1: Pop back to previous fragment
                findNavController().popBackStack()
            } catch (ex: Exception) {
                try {
                    // Fallback 2: Navigate to product picker by clearing stack
                    findNavController().popBackStack(R.id.fragmentProductPicker, false)
                } catch (ex2: Exception) {
                    // Final fallback: use activity back button
                    activity?.onBackPressed()
                }
            }
        }
    }

    private fun updateQuantityDisplay() {
        binding.tvQuantity.text = quantity.toString()
    }

    private fun updateTotalPrice() {
        val price = product?.price ?: cartItem?.price ?: 0.0
        val total = price * quantity
        binding.tvTotalPrice.text = "$${String.format("%.2f", total)}"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
