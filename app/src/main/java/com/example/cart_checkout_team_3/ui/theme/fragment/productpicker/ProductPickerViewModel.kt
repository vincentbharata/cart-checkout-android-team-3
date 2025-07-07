package com.example.cart_checkout_team_3.ui.theme.fragment.productpicker

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cart_checkout_team_3.model.Product
import com.example.cart_checkout_team_3.model.ProductResponse
import com.example.cart_checkout_team_3.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductPickerViewModel : ViewModel() {

    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> = _products

    val error = MutableLiveData<String>()

    fun getProducts() {
        ApiClient.apiService.getProducts().enqueue(object : Callback<ProductResponse> {
            override fun onResponse(
                call: Call<ProductResponse>, response: Response<ProductResponse>
            ) {
                if (response.isSuccessful) {
                    val productList = response.body()?.products ?: emptyList()
                    Log.d("ProductPickerVM", "Produk berhasil diambil: ${productList.size} item")
                    _products.value = productList
                } else {
                    error.value = "Gagal load produk (${response.code()})"
                    Log.e("ProductPickerVM", "Gagal load produk: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                error.value = t.message ?: "Unknown error"
                Log.e("ProductPickerVM", "Network error: ${t.message}")
            }
        })
    }

}
