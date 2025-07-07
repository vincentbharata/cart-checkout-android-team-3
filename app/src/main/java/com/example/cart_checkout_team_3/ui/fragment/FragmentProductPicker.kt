package com.example.cart_checkout_team_3.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.cart_checkout_team_3.R
import com.example.cart_checkout_team_3.data.models.Product
import com.example.cart_checkout_team_3.databinding.FragmentProductPickerBinding
import com.example.cart_checkout_team_3.ui.adapter.ProductAdapter
import com.example.cart_checkout_team_3.ui.viewmodel.CartViewModel

class FragmentProductPicker : Fragment() {

    private var _binding: FragmentProductPickerBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CartViewModel by activityViewModels()
    private lateinit var productAdapter: ProductAdapter
    private var allProducts = listOf<Product>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductPickerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupSearchView()
        observeViewModel()

        // Only load products if user is logged in and products are empty
        viewModel.user.value?.let { user ->
            if (viewModel.products.value.isNullOrEmpty()) {
                viewModel.loadProducts()
            }
        }
    }

    private fun setupRecyclerView() {
        productAdapter = ProductAdapter(
            onAddToCart = { product ->
                try {
                    val bundle = Bundle().apply {
                        putParcelable("product", product)
                    }
                    findNavController().navigate(R.id.fragmentQuantitySelector, bundle)
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "Navigation error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            },
            onProductClick = { product ->
                try {
                    val bundle = Bundle().apply {
                        putParcelable("product", product)
                    }
                    findNavController().navigate(R.id.fragmentQuantitySelector, bundle)
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "Navigation error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        )

        binding.rvProducts.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = productAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupSearchView() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                try {
                    val query = s?.toString() ?: ""
                    filterProducts(query)
                } catch (e: Exception) {
                    android.util.Log.e("ProductPicker", "Error in search filter", e)
                    // Show original products list if filtering fails
                    if (::productAdapter.isInitialized) {
                        productAdapter.submitList(allProducts)
                    }
                }
            }
        })
    }

    private fun observeViewModel() {
        viewModel.products.observe(viewLifecycleOwner) { products ->
            try {
                allProducts = products ?: emptyList()

                if (::productAdapter.isInitialized) {
                    productAdapter.submitList(allProducts)
                }

                if (allProducts.isEmpty()) {
                    showEmptyView()
                } else {
                    showProductsList()
                }
            } catch (e: Exception) {
                android.util.Log.e("ProductPicker", "Error updating products", e)
                showEmptyView()
            }
        }

        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            try {
                binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            } catch (e: Exception) {
                android.util.Log.e("ProductPicker", "Error updating loading state", e)
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                try {
                    Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
                    viewModel.clearError()
                } catch (e: Exception) {
                    android.util.Log.e("ProductPicker", "Error showing error message", e)
                }
            }
        }

        // Observe user to ensure products are loaded for logged in user
        viewModel.user.observe(viewLifecycleOwner) { user ->
            try {
                if (user != null && viewModel.products.value.isNullOrEmpty()) {
                    viewModel.loadProducts()
                }
            } catch (e: Exception) {
                android.util.Log.e("ProductPicker", "Error loading products for user", e)
            }
        }
    }

    private fun filterProducts(query: String) {
        try {
            // Ensure we have valid data before filtering
            if (!::productAdapter.isInitialized) {
                android.util.Log.w("ProductPicker", "ProductAdapter not initialized, skipping filter")
                return
            }

            val safeQuery = query.trim()
            val filteredProducts = if (safeQuery.isEmpty()) {
                allProducts
            } else {
                allProducts.filter { product ->
                    try {
                        val title = product.title ?: ""
                        val description = product.description ?: ""
                        val category = product.category ?: ""
                        val brand = product.brand ?: ""

                        title.contains(safeQuery, ignoreCase = true) ||
                                description.contains(safeQuery, ignoreCase = true) ||
                                category.contains(safeQuery, ignoreCase = true) ||
                                brand.contains(safeQuery, ignoreCase = true)
                    } catch (e: Exception) {
                        android.util.Log.e("ProductPicker", "Error filtering product: ${product.title}", e)
                        false
                    }
                }
            }

            productAdapter.submitList(filteredProducts) {
                // Callback after list is submitted
                if (filteredProducts.isEmpty() && safeQuery.isNotEmpty()) {
                    showNoSearchResults()
                } else if (filteredProducts.isNotEmpty()) {
                    showProductsList()
                }
            }

        } catch (e: Exception) {
            android.util.Log.e("ProductPicker", "Error in filterProducts", e)
            // Fallback to show all products if filtering fails
            if (::productAdapter.isInitialized) {
                productAdapter.submitList(allProducts)
            }
        }
    }

    private fun showEmptyView() {
        try {
            binding.apply {
                rvProducts.visibility = View.GONE
                layoutEmptyProducts?.visibility = View.VISIBLE
            }
        } catch (e: Exception) {
            android.util.Log.e("ProductPicker", "Error showing empty view", e)
        }
    }

    private fun showProductsList() {
        try {
            binding.apply {
                rvProducts.visibility = View.VISIBLE
                layoutEmptyProducts?.visibility = View.GONE
            }
        } catch (e: Exception) {
            android.util.Log.e("ProductPicker", "Error showing products list", e)
        }
    }

    private fun showNoSearchResults() {
        try {
            binding.apply {
                rvProducts.visibility = View.GONE
                layoutEmptyProducts?.visibility = View.VISIBLE
                // You can customize the empty view message for "no search results"
            }
        } catch (e: Exception) {
            android.util.Log.e("ProductPicker", "Error showing no search results", e)
        }
    }

    override fun onResume() {
        super.onResume()
        // Don't automatically reload products on resume to prevent multiple calls
        // Products will be loaded when needed through user interactions
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
