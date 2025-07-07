package com.example.cart_checkout_team_3.model

data class AddToCartRequest(
    val userId: Int,
    val products: List<CartProduct>
)
