package com.example.cart_checkout_team_3.data.repository

import com.example.cart_checkout_team_3.data.api.ApiClient
import com.example.cart_checkout_team_3.data.local.AppDatabase
import com.example.cart_checkout_team_3.data.local.CartHistoryEntity
import com.example.cart_checkout_team_3.data.local.CartItemEntity
import com.example.cart_checkout_team_3.data.models.*
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import java.util.Date

class CartRepository(private val database: AppDatabase) {

    private val apiService = ApiClient.apiService
    private val cartHistoryDao = database.cartHistoryDao()

    // API calls with proper error handling
    suspend fun login(username: String, password: String): Response<LoginResponse> {
        return apiService.login(LoginRequest(username, password))
    }

    suspend fun getProducts(): Response<ProductResponse> {
        return apiService.getProducts()
    }

    suspend fun getProduct(id: Int): Response<Product> {
        return apiService.getProduct(id)
    }

    suspend fun addToCart(userId: Int, products: List<CartProductRequest>): Response<Cart> {
        return apiService.addToCart(AddToCartRequest(userId, products))
    }

    suspend fun getCartByUser(userId: Int): Response<CartResponse> {
        return apiService.getCartByUser(userId)
    }

    suspend fun updateCart(cartId: Int, userId: Int, products: List<CartProductRequest>): Response<Cart> {
        return apiService.updateCart(cartId, AddToCartRequest(userId, products))
    }

    suspend fun deleteCart(cartId: Int): Response<Cart> {
        return apiService.deleteCart(cartId)
    }

    // Local database operations
    fun getCartHistory(userId: Int): Flow<List<CartHistoryEntity>> {
        return cartHistoryDao.getCartHistoryByUser(userId)
    }

    fun getAllCartHistory(): Flow<List<CartHistoryEntity>> {
        return cartHistoryDao.getAllCartHistory()
    }

    suspend fun saveCartHistory(cart: Cart, status: String = "completed"): Long {
        val cartItems = cart.products.map { cartItem ->
            CartItemEntity(
                productId = cartItem.id,
                title = cartItem.title,
                price = cartItem.price,
                quantity = cartItem.quantity,
                total = cartItem.total,
                thumbnail = cartItem.thumbnail
            )
        }

        val cartHistory = CartHistoryEntity(
            userId = cart.userId,
            products = cartItems,
            total = cart.total,
            discountedTotal = cart.discountedTotal,
            totalProducts = cart.totalProducts,
            totalQuantity = cart.totalQuantity,
            timestamp = Date(),
            status = status
        )

        return cartHistoryDao.insertCartHistory(cartHistory)
    }

    suspend fun updateCartHistoryStatus(cartHistory: CartHistoryEntity, status: String) {
        cartHistoryDao.updateCartHistory(cartHistory.copy(status = status))
    }

    suspend fun deleteCartHistory(cartHistory: CartHistoryEntity) {
        cartHistoryDao.deleteCartHistory(cartHistory)
    }
}
