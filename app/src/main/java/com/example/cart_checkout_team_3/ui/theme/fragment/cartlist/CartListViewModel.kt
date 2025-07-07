package com.example.cart_checkout_team_3.ui.theme.fragment.cartlist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cart_checkout_team_3.model.Cart
import com.example.cart_checkout_team_3.model.CartProductResponse
import com.example.cart_checkout_team_3.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartListViewModel : ViewModel() {

    private val _cartItems = MutableLiveData<List<CartProductResponse>>()
    val cartItems: LiveData<List<CartProductResponse>> = _cartItems

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun loadCart(userId: Int) {
        ApiClient.apiService.getCartByUser(userId).enqueue(object : Callback<Cart> {
            override fun onResponse(call: Call<Cart>, response: Response<Cart>) {
                if (response.isSuccessful) {
                    _cartItems.value = response.body()?.products ?: emptyList()
                } else {
                    Log.e("CartListVM", "Gagal load cart: ${response.code()}")
                    _cartItems.value = emptyList()
                }
            }

            override fun onFailure(call: Call<Cart>, t: Throwable) {
                Log.e("CartListVM", "Network error: ${t.message}")
                _cartItems.value = emptyList()
            }
        })
    }

}
