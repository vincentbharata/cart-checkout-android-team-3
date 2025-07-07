package com.example.cart_checkout_team_3.ui.theme.fragment.cartsummary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.cart_checkout_team_3.R
import com.example.cart_checkout_team_3.databinding.FragmentCartSummaryBinding
import com.example.cart_checkout_team_3.model.Cart
import com.example.cart_checkout_team_3.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartSummaryFragment : Fragment() {

    private var _binding: FragmentCartSummaryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartSummaryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        fetchCartSummary()

        binding.btnConfirmCheckout.setOnClickListener {
            Toast.makeText(requireContext(), "Checkout successful!", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.checkoutResultFragment)
        }
    }

    private fun fetchCartSummary() {
        ApiClient.apiService.getCartByUser(1).enqueue(object : Callback<Cart> {
            override fun onResponse(call: Call<Cart>, response: Response<Cart>) {
                if (response.isSuccessful) {
                    val cart = response.body()
                    binding.tvTotalItems.text = "Total Items: ${cart?.totalProducts ?: 0}"
                    binding.tvTotalPrice.text = "Total Price: $${cart?.total ?: 0}"
                } else {
                    Toast.makeText(requireContext(), "Failed to load cart", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Cart>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
