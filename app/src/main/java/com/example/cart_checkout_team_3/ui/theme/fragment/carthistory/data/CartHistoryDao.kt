package com.example.cart_checkout_team_3.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*

import com.example.cart_checkout_team_3.ui.theme.fragment.carthistory.data.CartHistoryEntity

@Dao
interface CartHistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cart: CartHistoryEntity)

    @Query("SELECT * FROM cart_history ORDER BY id DESC")
    fun getAll(): LiveData<List<CartHistoryEntity>>

    @Query("DELETE FROM cart_history")
    suspend fun clear()
}
