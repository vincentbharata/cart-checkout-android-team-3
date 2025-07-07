package com.example.cart_checkout_team_3.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cart_checkout_team_3.R
import com.example.cart_checkout_team_3.data.models.CartItem
import com.example.cart_checkout_team_3.databinding.ItemCartBinding

class CartAdapter(
    private val onQuantityChanged: (Int, Int) -> Unit,
    private val onRemoveItem: (Int) -> Unit,
    private val onItemClick: (CartItem) -> Unit
) : ListAdapter<CartItem, CartAdapter.CartViewHolder>(CartDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CartViewHolder(private val binding: ItemCartBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(cartItem: CartItem) {
            binding.apply {
                tvProductTitle.text = cartItem.title
                tvProductPrice.text = "$${String.format("%.2f", cartItem.price)}"
                tvProductTotal.text = "Total: $${String.format("%.2f", cartItem.total)}"
                tvQuantity.text = cartItem.quantity.toString()

                // Load product image
                Glide.with(itemView.context)
                    .load(cartItem.thumbnail)
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.placeholder_image)
                    .into(ivProductImage)

                // Quantity controls
                btnIncrease.setOnClickListener {
                    onQuantityChanged(cartItem.id, cartItem.quantity + 1)
                }

                btnDecrease.setOnClickListener {
                    if (cartItem.quantity > 1) {
                        onQuantityChanged(cartItem.id, cartItem.quantity - 1)
                    }
                }

                // Remove item
                btnRemove.setOnClickListener {
                    onRemoveItem(cartItem.id)
                }

                // Item click
                root.setOnClickListener {
                    onItemClick(cartItem)
                }

                // Enable/disable decrease button
                btnDecrease.isEnabled = cartItem.quantity > 1
            }
        }
    }
}

class CartDiffCallback : DiffUtil.ItemCallback<CartItem>() {
    override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
        return oldItem == newItem
    }
}
