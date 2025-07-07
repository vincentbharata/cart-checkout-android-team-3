package com.example.cart_checkout_team_3.ui.theme.fragment.productpicker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cart_checkout_team_3.databinding.FragmentProductPickerBinding
import com.example.cart_checkout_team_3.model.Product

class ProductPickerFragment : Fragment() {

    private var _binding: FragmentProductPickerBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ProductPickerViewModel
    private lateinit var adapter: ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductPickerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this)[ProductPickerViewModel::class.java]
        setupRecyclerView()

        viewModel.products.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        viewModel.error.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), "Error: $it", Toast.LENGTH_SHORT).show()
        }

        viewModel.getProducts()
    }

    private fun setupRecyclerView() {
        adapter = ProductAdapter { selectedProduct ->
            // Navigasi ke AddToCartFragment dengan productId
            val action = ProductPickerFragmentDirections
                .actionProductPickerFragmentToAddToCartFragment(selectedProduct.id, selectedProduct.title)
            findNavController().navigate(action)
        }

        binding.rvProducts.layoutManager = LinearLayoutManager(requireContext())
        binding.rvProducts.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
