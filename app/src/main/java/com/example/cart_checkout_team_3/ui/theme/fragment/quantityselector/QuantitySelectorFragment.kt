package com.example.cart_checkout_team_3.ui.theme.fragment.quantityselector

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.cart_checkout_team_3.databinding.FragmentQuantitySelectorBinding

class QuantitySelectorFragment : Fragment() {

    private var _binding: FragmentQuantitySelectorBinding? = null
    private val binding get() = _binding!!

    private var quantity = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuantitySelectorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateQuantity()

        binding.btnPlus.setOnClickListener {
            quantity++
            updateQuantity()
        }

        binding.btnMinus.setOnClickListener {
            if (quantity > 1) {
                quantity--
                updateQuantity()
            }
        }

        binding.btnConfirm.setOnClickListener {
            // Lakukan sesuatu dengan quantity, misal: kirim balik ke fragment sebelumnya
            // atau simpan ke ViewModel
        }
    }

    private fun updateQuantity() {
        binding.tvQuantity.text = quantity.toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
