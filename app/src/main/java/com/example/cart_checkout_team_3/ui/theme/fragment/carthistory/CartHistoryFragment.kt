package com.example.cart_checkout_team_3.ui.theme.fragment.carthistory

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cart_checkout_team_3.databinding.FragmentCartHistoryBinding

class CartHistoryFragment : Fragment() {

    private var _binding: FragmentCartHistoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: CartHistoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartHistoryBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[CartHistoryViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.cartList.observe(viewLifecycleOwner) { history ->
            binding.rvCartHistory.layoutManager = LinearLayoutManager(requireContext())
            binding.rvCartHistory.adapter = CartHistoryAdapter(history)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
