package com.example.cart_checkout_team_3.network

import com.example.cart_checkout_team_3.model.AddToCartRequest
import com.example.cart_checkout_team_3.model.Cart
import com.example.cart_checkout_team_3.model.LoginRequest
import com.example.cart_checkout_team_3.model.LoginResponse
import com.example.cart_checkout_team_3.model.ProductResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @POST("auth/login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @GET("products")
    fun getProducts(): Call<ProductResponse>

    @POST("carts/add")
    fun addToCart(@Body body: AddToCartRequest): Call<Any>

    @GET("carts/user/{userId}")
    fun getCartByUser(@Path("userId") userId: Int): Call<Cart>

}
