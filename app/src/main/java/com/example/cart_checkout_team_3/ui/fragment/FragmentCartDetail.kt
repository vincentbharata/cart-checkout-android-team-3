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
import com.example.cart_checkout_team_3.data.models.CartItem
import com.example.cart_checkout_team_3.databinding.FragmentCartDetailBinding
import com.example.cart_checkout_team_3.ui.viewmodel.CartViewModel

class FragmentCartDetail : Fragment() {

    private var _binding: FragmentCartDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CartViewModel by activityViewModels()
    private var cartItem: CartItem? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get cart item from arguments
        cartItem = arguments?.getParcelable("cart_item")

        cartItem?.let {
            setupCartItemDetails(it)
        } ?: run {
            Toast.makeText(requireContext(), "No item data available", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
        }

        setupClickListeners()
        observeViewModel()
    }

    private fun setupCartItemDetails(item: CartItem) {
        binding.apply {
            tvProductTitle.text = item.title
            tvProductPrice.text = "$${String.format("%.2f", item.price)}"
            tvQuantity.text = item.quantity.toString()
            tvTotalPrice.text = "$${String.format("%.2f", item.total)}"

            // Load product image
            Glide.with(requireContext())
                .load(item.thumbnail)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_gallery)
                .into(ivProductImage)
        }
    }

    private fun setupClickListeners() {
        binding.apply {
            btnEditQuantity?.setOnClickListener {
                cartItem?.let { item ->
                    val bundle = Bundle().apply {
                        putParcelable("cart_item", item)
                    }
                    findNavController().navigate(R.id.fragmentQuantitySelector, bundle)
                }
            }

            btnRemoveItem?.setOnClickListener {
                cartItem?.let { item ->
                    androidx.appcompat.app.AlertDialog.Builder(requireContext())
                        .setTitle("Remove Item")
                        .setMessage("Are you sure you want to remove this item from cart?")
                        .setPositiveButton("Yes") { _, _ ->
                            viewModel.removeFromCart(item.id)
                            Toast.makeText(requireContext(), "Item removed from cart", Toast.LENGTH_SHORT).show()
                            findNavController().navigateUp()
                        }
                        .setNegativeButton("No", null)
                        .show()
                }
            }
        }
    }

    private fun observeViewModel() {
        viewModel.cartItems.observe(viewLifecycleOwner) { cartItems ->
            cartItem?.let { currentItem ->
                val updatedItem = cartItems.find { it.id == currentItem.id }
                if (updatedItem == null) {
                    // Item was removed, go back
                    findNavController().navigateUp()
                } else if (updatedItem != currentItem) {
                    // Item was updated, refresh display
                    cartItem = updatedItem
                    setupCartItemDetails(updatedItem)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
