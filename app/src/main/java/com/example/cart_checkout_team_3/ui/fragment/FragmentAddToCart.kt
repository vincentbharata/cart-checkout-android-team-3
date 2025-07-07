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
import com.example.cart_checkout_team_3.databinding.FragmentAddToCartBinding
import com.example.cart_checkout_team_3.ui.viewmodel.CartViewModel

class FragmentAddToCart : Fragment() {

    private var _binding: FragmentAddToCartBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CartViewModel by activityViewModels()
    private var product: Product? = null
    private var quantity = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddToCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get product from arguments
        product = arguments?.getParcelable("product")

        product?.let { setupProductDetails(it) }
        setupClickListeners()
        observeViewModel()
        updateTotalPrice()
    }

    private fun setupProductDetails(product: Product) {
        binding.apply {
            tvProductTitle.text = product.title
            tvProductCategory.text = product.category.replaceFirstChar { it.uppercase() }
            tvProductBrand.text = product.brand
            tvProductDescription.text = product.description
            tvProductPrice.text = "$${String.format("%.2f", product.price)}"
            tvProductRating.text = String.format("%.1f", product.rating)

            // Load product image
            Glide.with(requireContext())
                .load(product.thumbnail)
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.placeholder_image)
                .into(ivProductImage)
        }
    }

    private fun setupClickListeners() {
        binding.apply {
            btnIncrease.setOnClickListener {
                if (quantity < 10) { // Max quantity limit
                    quantity++
                    updateQuantityDisplay()
                    updateTotalPrice()
                }
            }

            btnDecrease.setOnClickListener {
                if (quantity > 1) {
                    quantity--
                    updateQuantityDisplay()
                    updateTotalPrice()
                }
            }

            btnAddToCart.setOnClickListener {
                product?.let { prod ->
                    viewModel.addToCart(prod.id, quantity)
                }
            }
        }
    }

    private fun observeViewModel() {
        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            binding.btnAddToCart.isEnabled = !isLoading
            binding.btnAddToCart.text = if (isLoading) "Adding..." else "Add to Cart"
        }

        viewModel.cart.observe(viewLifecycleOwner) { cart ->
            if (cart != null) {
                Toast.makeText(requireContext(), "Product added to cart!", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
                viewModel.clearError()
            }
        }
    }

    private fun updateQuantityDisplay() {
        binding.tvQuantity.text = quantity.toString()
        binding.btnDecrease.isEnabled = quantity > 1
    }

    private fun updateTotalPrice() {
        product?.let { prod ->
            val total = prod.price * quantity
            binding.tvTotalPrice.text = "$${String.format("%.2f", total)}"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
