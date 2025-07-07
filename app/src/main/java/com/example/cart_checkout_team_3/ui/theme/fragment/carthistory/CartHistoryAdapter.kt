package com.example.cart_checkout_team_3.ui.theme.fragment.carthistory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cart_checkout_team_3.databinding.ItemCartHistoryBinding
import com.example.cart_checkout_team_3.ui.theme.fragment.carthistory.data.CartHistoryEntity

class CartHistoryAdapter(private val data: List<CartHistoryEntity>) :
    RecyclerView.Adapter<CartHistoryAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemCartHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CartHistoryEntity) {
            binding.tvSummary.text = item.summary
            binding.tvTotal.text = "Total: $${item.total}"
            binding.tvDate.text = item.date
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCartHistoryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }
}
