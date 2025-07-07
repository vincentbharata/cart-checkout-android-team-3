package com.example.cart_checkout_team_3.ui.theme.fragment.cartlist

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cart_checkout_team_3.databinding.ItemCartProductBinding
import com.example.cart_checkout_team_3.model.CartProductResponse

class CartItemAdapter : RecyclerView.Adapter<CartItemAdapter.CartViewHolder>() {

    private val items = mutableListOf<CartProductResponse>()

    fun submitList(data: List<CartProductResponse>?) {
        Log.d("CartItemAdapter", "Received data: $data")
        items.clear()
        items.addAll(data ?: emptyList())
        notifyDataSetChanged()
    }


    class CartViewHolder(val binding: ItemCartProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: CartProductResponse) {
            binding.tvProductName.text = product.title
            binding.tvQuantity.text = "Qty: ${product.quantity}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ItemCartProductBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

}
