package com.example.cart_checkout_team_3.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cart_checkout_team_3.R
import com.example.cart_checkout_team_3.databinding.FragmentCartHistoryBinding
import com.example.cart_checkout_team_3.ui.adapter.CartHistoryAdapter
import com.example.cart_checkout_team_3.ui.viewmodel.CartViewModel
import kotlinx.coroutines.launch

class FragmentCartHistory : Fragment() {

    private var _binding: FragmentCartHistoryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CartViewModel by activityViewModels()
    private lateinit var historyAdapter: CartHistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeCartHistory()
    }

    private fun setupRecyclerView() {
        historyAdapter = CartHistoryAdapter(
            onViewDetails = { cartHistory ->
                // Convert CartItemEntity to display format
                val itemsText = cartHistory.products.joinToString("\n") {
                    "• ${it.title} x${it.quantity} = $${String.format("%.2f", it.total)}"
                }
                val detailMessage = """
                    Order #${cartHistory.id}
                    Date: ${java.text.SimpleDateFormat("MMM dd, yyyy HH:mm", java.util.Locale.getDefault()).format(cartHistory.timestamp)}
                    Status: ${cartHistory.status.uppercase()}
                    
                    Items:
                    $itemsText
                    
                    Subtotal: $${String.format("%.2f", cartHistory.total)}
                    Total: $${String.format("%.2f", cartHistory.discountedTotal)}
                """.trimIndent()

                androidx.appcompat.app.AlertDialog.Builder(requireContext())
                    .setTitle("Order Details")
                    .setMessage(detailMessage)
                    .setPositiveButton("OK", null)
                    .show()
            },
            onReorder = { cartHistory ->
                // Add all items from history back to cart
                try {
                    cartHistory.products.forEach { historyItem ->
                        viewModel.addToCart(historyItem.productId, historyItem.quantity)
                    }
                    Toast.makeText(requireContext(), "${cartHistory.products.size} items added to cart", Toast.LENGTH_SHORT).show()

                    // Navigate to cart after reordering
                    try {
                        findNavController().navigate(R.id.fragmentCartList)
                    } catch (e: Exception) {
                        // If navigation fails, just show success message
                        Toast.makeText(requireContext(), "Items added successfully", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "Error reordering: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        )

        binding.rvCartHistory.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = historyAdapter
            setHasFixedSize(true)
        }
    }

    private fun observeCartHistory() {
        lifecycleScope.launch {
            try {
                viewModel.getCartHistory().collect { historyList ->
                    historyAdapter.submitList(historyList)

                    // Update header count
                    updateHistoryCount(historyList.size)

                    if (historyList.isEmpty()) {
                        showEmptyHistory()
                    } else {
                        showHistoryList()
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error loading history: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun updateHistoryCount(count: Int) {
        binding.tvHistoryCount.text = if (count == 0) {
            "No orders found"
        } else {
            "$count order${if (count > 1) "s" else ""} found"
        }
    }

    private fun showEmptyHistory() {
        binding.rvCartHistory.visibility = View.GONE
        // Show simple empty message
        Toast.makeText(requireContext(), "No order history found", Toast.LENGTH_SHORT).show()
    }

    private fun showHistoryList() {
        binding.rvCartHistory.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
