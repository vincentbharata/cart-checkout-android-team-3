package com.example.cart_checkout_team_3.ui.theme.fragment.carthistory

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.cart_checkout_team_3.ui.theme.fragment.carthistory.data.*
import kotlinx.coroutines.launch

class CartHistoryViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = CartHistoryDatabase.getDatabase(application).cartHistoryDao()
    val cartList: LiveData<List<CartHistoryEntity>> = dao.getAll()

    fun addHistory(summary: String, total: Int, date: String) {
        viewModelScope.launch {
            dao.insert(CartHistoryEntity(summary = summary, total = total, date = date))
        }
    }
}
