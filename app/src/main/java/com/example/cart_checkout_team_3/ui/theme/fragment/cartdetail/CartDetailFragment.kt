package com.example.cart_checkout_team_3.ui.theme.fragment.cartdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.cart_checkout_team_3.databinding.FragmentCartDetailBinding

class CartDetailFragment : Fragment() {

    private var _binding: FragmentCartDetailBinding? = null
    private val binding get() = _binding!!

    // Data ini biasanya dikirim dari Fragment sebelumnya via SafeArgs
    private var productName: String? = null
    private var productPrice: Int? = null
    private var productQuantity: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Contoh jika pakai SafeArgs
        arguments?.let { bundle ->
            val args = CartDetailFragmentArgs.fromBundle(bundle)
            productName = args.productName
            productPrice = args.productPrice
            productQuantity = args.productQuantity
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvProductName.text = productName ?: "N/A"
        binding.tvPrice.text = "Price: $${productPrice ?: 0}"
        binding.tvQuantity.text = "Quantity: ${productQuantity ?: 0}"
        binding.tvTotal.text = "Total: $${(productPrice ?: 0) * (productQuantity ?: 0)}"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
