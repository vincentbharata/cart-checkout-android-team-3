package com.example.cart_checkout_team_3.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cart_checkout_team_3.R
import com.example.cart_checkout_team_3.data.models.Product
import com.example.cart_checkout_team_3.databinding.ItemProductBinding

class ProductAdapter(
    private val onAddToCart: (Product) -> Unit,
    private val onProductClick: (Product) -> Unit
) : ListAdapter<Product, ProductAdapter.ProductViewHolder>(ProductDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ProductViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            binding.apply {
                tvProductTitle.text = product.title
                tvProductCategory.text = product.category.replaceFirstChar { it.uppercase() }
                tvProductPrice.text = "$${String.format("%.2f", product.price)}"
                tvProductRating.text = String.format("%.1f", product.rating)

                // Load product image with better placeholder handling
                Glide.with(itemView.context)
                    .load(if (product.thumbnail.isEmpty() || product.thumbnail == "placeholder") null else product.thumbnail)
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.placeholder_image)
                    .fallback(R.drawable.placeholder_image)
                    .into(ivProductImage)

                // Click listeners
                btnAddToCart.setOnClickListener {
                    onAddToCart(product)
                }

                root.setOnClickListener {
                    onProductClick(product)
                }
            }
        }
    }
}

class ProductDiffCallback : DiffUtil.ItemCallback<Product>() {
    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem == newItem
    }
}
