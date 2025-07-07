package com.example.cart_checkout_team_3.data.api

import com.example.cart_checkout_team_3.data.models.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @GET("products")
    suspend fun getProducts(): Response<ProductResponse>

    @GET("products/{id}")
    suspend fun getProduct(@Path("id") id: Int): Response<Product>

    @POST("carts/add")
    suspend fun addToCart(@Body addToCartRequest: AddToCartRequest): Response<Cart>

    @GET("carts/user/{userId}")
    suspend fun getCartByUser(@Path("userId") userId: Int): Response<CartResponse>

    @GET("carts/{id}")
    suspend fun getCart(@Path("id") cartId: Int): Response<Cart>

    @PUT("carts/{id}")
    suspend fun updateCart(@Path("id") cartId: Int, @Body addToCartRequest: AddToCartRequest): Response<Cart>

    @DELETE("carts/{id}")
    suspend fun deleteCart(@Path("id") cartId: Int): Response<Cart>
}
