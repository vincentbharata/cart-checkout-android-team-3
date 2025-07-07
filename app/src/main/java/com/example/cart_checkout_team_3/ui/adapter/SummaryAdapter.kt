package com.example.cart_checkout_team_3.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cart_checkout_team_3.R
import com.example.cart_checkout_team_3.data.models.CartItem
import com.example.cart_checkout_team_3.databinding.ItemSummaryBinding

class SummaryAdapter : ListAdapter<CartItem, SummaryAdapter.SummaryViewHolder>(CartDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SummaryViewHolder {
        val binding = ItemSummaryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SummaryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SummaryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class SummaryViewHolder(private val binding: ItemSummaryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(cartItem: CartItem) {
            binding.apply {
                tvProductTitle.text = cartItem.title
                tvQuantityPrice.text = "${cartItem.quantity} × $${String.format("%.2f", cartItem.price)}"
                tvItemTotal.text = "$${String.format("%.2f", cartItem.total)}"

                // Load product image
                Glide.with(itemView.context)
                    .load(cartItem.thumbnail)
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.placeholder_image)
                    .into(ivProductImage)
            }
        }
    }
}
