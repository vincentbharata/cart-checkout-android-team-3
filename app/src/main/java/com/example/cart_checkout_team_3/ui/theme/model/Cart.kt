package com.example.cart_checkout_team_3.model

data class Cart(
    val id: Int,
    val userId: Int,
    val products: List<CartProductResponse>,
    val total: Int,
    val discountedTotal: Int,
    val totalProducts: Int,
    val totalQuantity: Int
)

data class CartProductResponse(
    val id: Int,
    val title: String,
    val price: Int,
    val quantity: Int,
    val total: Int
)
