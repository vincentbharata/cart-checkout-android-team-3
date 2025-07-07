package com.example.cart_checkout_team_3.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CartItem(
    val id: Int,
    val title: String,
    val price: Double,
    val quantity: Int,
    val total: Double,
    val discountPercentage: Double,
    val discountedPrice: Double,
    val thumbnail: String
) : Parcelable

@Parcelize
data class Cart(
    val id: Int,
    val products: List<CartItem>,
    val total: Double,
    val discountedTotal: Double,
    val userId: Int,
    val totalProducts: Int,
    val totalQuantity: Int
) : Parcelable

data class CartResponse(
    val carts: List<Cart>,
    val total: Int,
    val skip: Int,
    val limit: Int
)

data class AddToCartRequest(
    val userId: Int,
    val products: List<CartProductRequest>
)

data class CartProductRequest(
    val id: Int,
    val quantity: Int
)
