package com.example.cart_checkout_team_3.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.cart_checkout_team_3.data.local.CartHistoryEntity
import com.example.cart_checkout_team_3.databinding.ItemCartHistoryBinding
import java.text.SimpleDateFormat
import java.util.*

class CartHistoryAdapter(
    private val onViewDetails: (CartHistoryEntity) -> Unit,
    private val onReorder: (CartHistoryEntity) -> Unit
) : ListAdapter<CartHistoryEntity, CartHistoryAdapter.HistoryViewHolder>(HistoryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemCartHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class HistoryViewHolder(private val binding: ItemCartHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(cartHistory: CartHistoryEntity) {
            binding.apply {
                tvOrderId.text = "Order #${cartHistory.id}"

                val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                tvOrderDate.text = dateFormat.format(cartHistory.timestamp)

                tvOrderStatus.text = cartHistory.status.replaceFirstChar { it.uppercase() }

                val itemCount = cartHistory.totalQuantity
                tvItemsCount.text = if (itemCount == 1) "1 item" else "$itemCount items"

                tvOrderTotal.text = "$${String.format("%.2f", cartHistory.discountedTotal)}"

                // Set status background based on status
                when (cartHistory.status.lowercase()) {
                    "completed" -> tvOrderStatus.setBackgroundResource(com.example.cart_checkout_team_3.R.drawable.status_completed_bg)
                    "pending" -> tvOrderStatus.setBackgroundResource(com.example.cart_checkout_team_3.R.drawable.status_pending_bg)
                    "cancelled" -> tvOrderStatus.setBackgroundResource(com.example.cart_checkout_team_3.R.drawable.status_cancelled_bg)
                }

                btnViewDetails.setOnClickListener {
                    onViewDetails(cartHistory)
                }

                btnReorder.setOnClickListener {
                    onReorder(cartHistory)
                }
            }
        }
    }
}

class HistoryDiffCallback : DiffUtil.ItemCallback<CartHistoryEntity>() {
    override fun areItemsTheSame(oldItem: CartHistoryEntity, newItem: CartHistoryEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CartHistoryEntity, newItem: CartHistoryEntity): Boolean {
        return oldItem == newItem
    }
}
