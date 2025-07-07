package com.example.cart_checkout_team_3.model

data class LoginResponse(
    val id: Int,
    val username: String,
    val firstName: String,
    val lastName: String,
    val gender: String,
    val image: String,
    val accessToken: String
)
