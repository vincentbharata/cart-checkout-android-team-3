package com.example.cart_checkout_team_3.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.cart_checkout_team_3.data.local.AppDatabase
import com.example.cart_checkout_team_3.data.local.CartHistoryEntity
import com.example.cart_checkout_team_3.data.models.*
import com.example.cart_checkout_team_3.data.repository.CartRepository
import com.example.cart_checkout_team_3.utils.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class CartViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = CartRepository(AppDatabase.getDatabase(application))
    private val userPreferences = UserPreferences(application)

    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> = _products

    private val _cart = MutableLiveData<Cart?>()
    val cart: LiveData<Cart?> = _cart

    private val _cartItems = MutableLiveData<List<CartItem>>()
    val cartItems: LiveData<List<CartItem>> = _cartItems

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> = _user

    private val _loginSuccess = MutableLiveData<Boolean>()
    val loginSuccess: LiveData<Boolean> = _loginSuccess

    private val _checkoutResult = MutableLiveData<String>()
    val checkoutResult: LiveData<String> = _checkoutResult

    // Add flag to prevent multiple simultaneous API calls
    private var isLoadingProducts = false

    init {
        loadUser()
        // Remove automatic default user creation
        // Users must login manually through LoginActivity
    }

    private fun loadUser() {
        _user.value = userPreferences.getUser()
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null

            try {
                // For demo purposes, accept any non-empty credentials
                if (username.isNotEmpty() && password.isNotEmpty()) {
                    // Create user with provided credentials
                    val user = User(
                        id = 1,
                        username = username,
                        email = "$username@example.com",
                        firstName = username.capitalize(),
                        lastName = "User",
                        gender = "unknown",
                        image = ""
                    )
                    userPreferences.saveUser(user, "demo_token_${username}")
                    _user.value = user
                    _loginSuccess.value = true

                    // Initialize empty cart for the user
                    loadCartForUser(user.id)

                    android.util.Log.d("CartViewModel", "Login successful for user: $username")
                } else {
                    _error.value = "Please enter valid credentials"
                    _loginSuccess.value = false
                }
            } catch (e: Exception) {
                android.util.Log.e("CartViewModel", "Login error", e)
                _error.value = "Login failed: ${e.message}"
                _loginSuccess.value = false
            } finally {
                _loading.value = false
            }
        }
    }

    fun clearError() {
        _error.value = null
    }

    fun loadProducts() {
        // Prevent multiple simultaneous API calls
        if (isLoadingProducts) return

        viewModelScope.launch {
            isLoadingProducts = true
            _loading.value = true
            _error.value = null

            try {
                android.util.Log.d("CartViewModel", "Starting to load products from API")
                val response = repository.getProducts()
                android.util.Log.d("CartViewModel", "API response received - isSuccessful: ${response.isSuccessful}, code: ${response.code()}")

                if (response.isSuccessful) {
                    val productResponse = response.body()
                    android.util.Log.d("CartViewModel", "Response body: $productResponse")

                    if (productResponse != null) {
                        val products = productResponse.products
                        android.util.Log.d("CartViewModel", "Products received: ${products.size} items")
                        _products.value = products
                    } else {
                        android.util.Log.e("CartViewModel", "Response body is null")
                        loadFallbackProducts()
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    android.util.Log.e("CartViewModel", "API Error - Code: ${response.code()}, Message: ${response.message()}, Body: $errorBody")
                    loadFallbackProducts()
                }
            } catch (e: UnknownHostException) {
                android.util.Log.e("CartViewModel", "Network error - UnknownHostException", e)
                _error.value = "No internet connection. Loading offline data..."
                loadFallbackProducts()
            } catch (e: SocketTimeoutException) {
                android.util.Log.e("CartViewModel", "Network error - SocketTimeoutException", e)
                _error.value = "Connection timeout. Loading offline data..."
                loadFallbackProducts()
            } catch (e: HttpException) {
                android.util.Log.e("CartViewModel", "Network error - HttpException", e)
                _error.value = "Network error. Loading offline data..."
                loadFallbackProducts()
            } catch (e: IOException) {
                android.util.Log.e("CartViewModel", "Network error - IOException", e)
                _error.value = "Network error. Loading offline data..."
                loadFallbackProducts()
            } catch (e: Exception) {
                android.util.Log.e("CartViewModel", "Unexpected error", e)
                _error.value = "An unexpected error occurred. Loading offline data..."
                loadFallbackProducts()
            } finally {
                _loading.value = false
                isLoadingProducts = false
            }
        }
    }

    private fun loadFallbackProducts() {
        // Provide fallback products when network is unavailable
        val fallbackProducts = listOf(
            Product(
                id = 1,
                title = "Wireless Bluetooth Headphones",
                description = "High-quality wireless headphones with noise cancellation",
                price = 29.99,
                discountPercentage = 10.0,
                rating = 4.5,
                stock = 50,
                brand = "TechSound",
                category = "electronics",
                thumbnail = "placeholder", // Use placeholder instead of empty string
                images = emptyList()
            ),
            Product(
                id = 2,
                title = "Premium Cotton T-Shirt",
                description = "Comfortable and stylish cotton t-shirt for everyday wear",
                price = 49.99,
                discountPercentage = 15.0,
                rating = 4.2,
                stock = 30,
                brand = "StyleWear",
                category = "fashion",
                thumbnail = "placeholder", // Use placeholder instead of empty string
                images = emptyList()
            ),
            Product(
                id = 3,
                title = "Smart Coffee Maker",
                description = "Programmable coffee maker with built-in timer and auto-brew",
                price = 19.99,
                discountPercentage = 5.0,
                rating = 4.8,
                stock = 100,
                brand = "HomeChef",
                category = "home",
                thumbnail = "placeholder", // Use placeholder instead of empty string
                images = emptyList()
            )
        )
        _products.value = fallbackProducts
        android.util.Log.d("CartViewModel", "Loaded ${fallbackProducts.size} fallback products")
    }

    fun addToCart(productId: Int, quantity: Int) {
        val userId = userPreferences.getUserId()
        if (userId == 0) {
            _error.value = "Please login first"
            return
        }

        viewModelScope.launch {
            _loading.value = true
            _error.value = null

            try {
                android.util.Log.d("CartViewModel", "Adding product $productId with quantity $quantity to cart")

                // Get current cart items (from local state)
                val currentCartItems = _cartItems.value?.toMutableList() ?: mutableListOf()

                // Find the product from the products list
                val product = _products.value?.find { it.id == productId }
                if (product == null) {
                    _error.value = "Product not found"
                    return@launch
                }

                // Check if product already exists in cart
                val existingItemIndex = currentCartItems.indexOfFirst { it.id == productId }

                if (existingItemIndex >= 0) {
                    // Update existing item quantity
                    val existingItem = currentCartItems[existingItemIndex]
                    val newQuantity = existingItem.quantity + quantity
                    val newTotal = product.price * newQuantity
                    val discountedPrice = product.price * (1 - product.discountPercentage / 100)

                    currentCartItems[existingItemIndex] = existingItem.copy(
                        quantity = newQuantity,
                        total = newTotal,
                        discountedPrice = discountedPrice * newQuantity
                    )
                } else {
                    // Add new item to cart
                    val discountedPrice = product.price * (1 - product.discountPercentage / 100)
                    val total = product.price * quantity

                    val cartItem = CartItem(
                        id = product.id,
                        title = product.title,
                        price = product.price,
                        quantity = quantity,
                        total = total,
                        discountPercentage = product.discountPercentage,
                        discountedPrice = discountedPrice * quantity,
                        thumbnail = product.thumbnail
                    )
                    currentCartItems.add(cartItem)
                }

                // Calculate cart totals
                val cartTotal = currentCartItems.sumOf { it.total }
                val cartDiscountedTotal = currentCartItems.sumOf { it.discountedPrice }
                val totalQuantity = currentCartItems.sumOf { it.quantity }

                // Create updated cart
                val updatedCart = Cart(
                    id = 1, // Local cart ID
                    products = currentCartItems,
                    total = cartTotal,
                    discountedTotal = cartDiscountedTotal,
                    userId = userId,
                    totalProducts = currentCartItems.size,
                    totalQuantity = totalQuantity
                )

                // Update local state
                _cart.value = updatedCart
                _cartItems.value = currentCartItems

                android.util.Log.d("CartViewModel", "Cart updated locally. Total items: ${currentCartItems.size}")

            } catch (e: Exception) {
                android.util.Log.e("CartViewModel", "Error adding to cart", e)
                _error.value = "Failed to add to cart: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }

    fun loadCartForUser(userId: Int) {
        // For now, we'll use local cart state since DummyJSON doesn't persist carts
        // In a real app, this would load from the actual API
        android.util.Log.d("CartViewModel", "Loading cart for user $userId (using local state)")

        // If no local cart exists, initialize empty cart
        if (_cart.value == null) {
            val emptyCart = Cart(
                id = 1,
                products = emptyList(),
                total = 0.0,
                discountedTotal = 0.0,
                userId = userId,
                totalProducts = 0,
                totalQuantity = 0
            )
            _cart.value = emptyCart
            _cartItems.value = emptyList()
        }
    }

    fun updateCartItemQuantity(productId: Int, newQuantity: Int) {
        if (newQuantity <= 0) {
            removeFromCart(productId)
            return
        }

        val currentCartItems = _cartItems.value?.toMutableList() ?: return
        val product = _products.value?.find { it.id == productId } ?: return

        // Find and update the item
        val itemIndex = currentCartItems.indexOfFirst { it.id == productId }
        if (itemIndex >= 0) {
            val discountedPrice = product.price * (1 - product.discountPercentage / 100)
            val newTotal = product.price * newQuantity

            currentCartItems[itemIndex] = currentCartItems[itemIndex].copy(
                quantity = newQuantity,
                total = newTotal,
                discountedPrice = discountedPrice * newQuantity
            )

            // Recalculate cart totals
            val cartTotal = currentCartItems.sumOf { it.total }
            val cartDiscountedTotal = currentCartItems.sumOf { it.discountedPrice }
            val totalQuantity = currentCartItems.sumOf { it.quantity }

            val updatedCart = _cart.value?.copy(
                products = currentCartItems,
                total = cartTotal,
                discountedTotal = cartDiscountedTotal,
                totalProducts = currentCartItems.size,
                totalQuantity = totalQuantity
            )

            _cart.value = updatedCart
            _cartItems.value = currentCartItems

            android.util.Log.d("CartViewModel", "Updated quantity for product $productId to $newQuantity")
        }
    }

    fun removeFromCart(productId: Int) {
        val currentCartItems = _cartItems.value?.toMutableList() ?: return

        // Remove the item
        currentCartItems.removeAll { it.id == productId }

        // Recalculate cart totals
        val cartTotal = currentCartItems.sumOf { it.total }
        val cartDiscountedTotal = currentCartItems.sumOf { it.discountedPrice }
        val totalQuantity = currentCartItems.sumOf { it.quantity }

        val updatedCart = _cart.value?.copy(
            products = currentCartItems,
            total = cartTotal,
            discountedTotal = cartDiscountedTotal,
            totalProducts = currentCartItems.size,
            totalQuantity = totalQuantity
        )

        _cart.value = updatedCart
        _cartItems.value = currentCartItems

        android.util.Log.d("CartViewModel", "Removed product $productId from cart")
    }

    fun checkout() {
        val currentCart = _cart.value
        if (currentCart != null) {
            viewModelScope.launch {
                _loading.value = true
                _error.value = null

                try {
                    // Save to local database as completed order
                    repository.saveCartHistory(currentCart, "completed")

                    // Clear the current cart
                    _cart.value = null
                    _cartItems.value = emptyList()

                    _checkoutResult.value = "Checkout successful! Order total: $${String.format("%.2f", currentCart.discountedTotal)}"
                } catch (e: Exception) {
                    _error.value = "Checkout failed: ${e.message}"
                } finally {
                    _loading.value = false
                }
            }
        } else {
            _error.value = "Cart is empty. Add some products first."
        }
    }

    fun getCartHistory(): Flow<List<CartHistoryEntity>> {
        val userId = userPreferences.getUserId()
        return repository.getCartHistory(userId)
    }

    fun logout() {
        userPreferences.logout()
        _user.value = null
        _cart.value = null
        _cartItems.value = emptyList()
        _products.value = emptyList()
        _error.value = null
    }

    fun refreshData() {
        val currentUser = _user.value
        if (currentUser != null) {
            loadProducts()
            loadCartForUser(currentUser.id)
        } else {
            loadProducts()
        }
    }
}
