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
                filterProducts(s.toString())
            }
        })
    }

    private fun observeViewModel() {
        viewModel.products.observe(viewLifecycleOwner) { products ->
            allProducts = products
            productAdapter.submitList(products)

            if (products.isEmpty()) {
                showEmptyView()
            } else {
                showProductsList()
            }
        }

        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
                viewModel.clearError()
            }
        }

        // Observe user to ensure products are loaded for logged in user
        viewModel.user.observe(viewLifecycleOwner) { user ->
            if (user != null && viewModel.products.value.isNullOrEmpty()) {
                viewModel.loadProducts()
            }
        }
    }

    private fun filterProducts(query: String) {
        val filteredProducts = if (query.isEmpty()) {
            allProducts
        } else {
            allProducts.filter { product ->
                product.title.contains(query, ignoreCase = true) ||
                product.description.contains(query, ignoreCase = true) ||
                product.category.contains(query, ignoreCase = true) ||
                product.brand.contains(query, ignoreCase = true)
            }
        }
        productAdapter.submitList(filteredProducts)
    }

    private fun showEmptyView() {
        binding.apply {
            rvProducts.visibility = View.GONE
            layoutEmptyProducts.visibility = View.VISIBLE
        }
    }

    private fun showProductsList() {
        binding.apply {
            rvProducts.visibility = View.VISIBLE
            layoutEmptyProducts.visibility = View.GONE
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
