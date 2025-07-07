package com.example.cart_checkout_team_3.ui.theme.fragment.checkoutresult

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.cart_checkout_team_3.R
import com.example.cart_checkout_team_3.databinding.FragmentCheckoutResultBinding

class CheckoutResultFragment : Fragment() {

    private var _binding: FragmentCheckoutResultBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCheckoutResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnBackToCart.setOnClickListener {
            findNavController().navigate(R.id.cartListFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
