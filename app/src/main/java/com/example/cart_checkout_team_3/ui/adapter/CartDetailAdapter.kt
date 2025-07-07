package com.example.cart_checkout_team_3.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cart_checkout_team_3.data.models.CartItem
import com.example.cart_checkout_team_3.databinding.ItemCartBinding

class CartDetailAdapter : ListAdapter<CartItem, CartDetailAdapter.DetailViewHolder>(DetailDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailViewHolder {
        val binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DetailViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class DetailViewHolder(private val binding: ItemCartBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CartItem) {
            binding.apply {
                tvProductTitle.text = item.title
                tvProductPrice.text = "$${String.format("%.2f", item.price)}"
                tvQuantity.text = item.quantity.toString()
                tvProductTotal.text = "Total: $${String.format("%.2f", item.total)}"

                // Hide interactive buttons in detail view
                btnIncrease.visibility = android.view.View.GONE
                btnDecrease.visibility = android.view.View.GONE
                btnRemove.visibility = android.view.View.GONE

                // Load product image
                Glide.with(itemView.context)
                    .load(item.thumbnail)
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.ic_menu_gallery)
                    .into(ivProductImage)
            }
        }
    }

    private class DetailDiffCallback : DiffUtil.ItemCallback<CartItem>() {
        override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem == newItem
        }
    }
}
