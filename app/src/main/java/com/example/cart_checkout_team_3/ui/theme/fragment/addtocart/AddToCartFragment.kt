package com.example.cart_checkout_team_3.ui.theme.fragment.addtocart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.cart_checkout_team_3.R
import com.example.cart_checkout_team_3.databinding.FragmentAddToCartBinding
import com.example.cart_checkout_team_3.model.AddToCartRequest
import com.example.cart_checkout_team_3.model.CartProduct
import com.example.cart_checkout_team_3.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddToCartFragment : Fragment() {

    private var _binding: FragmentAddToCartBinding? = null
    private val binding get() = _binding!!

    private val args: AddToCartFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddToCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvSelectedProduct.text = "Product: ${args.productName}"

        binding.btnAddToCart.setOnClickListener {
            val qty = binding.etQuantity.text.toString().toIntOrNull()
            if (qty == null || qty <= 0) {
                Toast.makeText(requireContext(), "Quantity must be > 0", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val userId = args.userId // Now using camelCase userId

            val body = AddToCartRequest(
                userId = userId,
                products = listOf(CartProduct(args.productId, qty))
            )

            ApiClient.apiService.addToCart(body).enqueue(object : Callback<Any> {
                override fun onResponse(call: Call<Any>, response: Response<Any>) {
                    if (response.isSuccessful) {
                        Toast.makeText(requireContext(), "Added to cart!", Toast.LENGTH_SHORT).show()

                        // Navigate to CartListFragment
                        val action = AddToCartFragmentDirections
                            .actionAddToCartFragmentToCartListFragment()

                        findNavController().navigate(action)
                    } else {
                        Toast.makeText(requireContext(), "Failed to add", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Any>, t: Throwable) {
                    Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}