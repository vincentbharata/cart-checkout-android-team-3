package com.example.cart_checkout_team_3.ui.theme.fragment.cartlist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cart_checkout_team_3.R
import com.example.cart_checkout_team_3.databinding.FragmentCartListBinding
import com.example.cart_checkout_team_3.model.Cart
import com.example.cart_checkout_team_3.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartListFragment : Fragment() {

    private var _binding: FragmentCartListBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: CartItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = CartItemAdapter()
        binding.rvCartItems.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCartItems.adapter = adapter

        val userId = arguments?.getInt("USER_ID") ?: -1
        Log.d("CartListFragment", "USER_ID received in Fragment: $userId")

        if (userId == -1) {
            Toast.makeText(requireContext(), "Invalid user ID!", Toast.LENGTH_LONG).show()
        }

        fetchCart(userId)

        binding.btnCheckout.setOnClickListener {
            findNavController().navigate(R.id.cartSummaryFragment)
        }
    }

    private fun fetchCart(userId: Int) {
        Log.d("CartListFragment", "Fetching cart for userId=$userId")

        ApiClient.apiService.getCartByUser(userId).enqueue(object : Callback<Cart> {
            override fun onResponse(call: Call<Cart>, response: Response<Cart>) {
                if (response.isSuccessful) {
                    val cart = response.body()
                    Log.d("CartListFragment", "Cart fetched: ${cart?.products?.size} items")
                    cart?.let {
                        adapter.submitList(it.products)
                    }
                } else {
                    Log.e("CartListFragment", "Failed to load cart: ${response.code()}")
                    Toast.makeText(requireContext(), "Failed to load cart", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Cart>, t: Throwable) {
                Log.e("CartListFragment", "Error loading cart", t)
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
