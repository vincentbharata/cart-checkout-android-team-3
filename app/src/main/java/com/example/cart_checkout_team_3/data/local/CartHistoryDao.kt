package com.example.cart_checkout_team_3.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CartHistoryDao {
    @Query("SELECT * FROM cart_history WHERE userId = :userId ORDER BY timestamp DESC")
    fun getCartHistoryByUser(userId: Int): Flow<List<CartHistoryEntity>>

    @Query("SELECT * FROM cart_history ORDER BY timestamp DESC")
    fun getAllCartHistory(): Flow<List<CartHistoryEntity>>

    @Insert
    suspend fun insertCartHistory(cartHistory: CartHistoryEntity): Long

    @Update
    suspend fun updateCartHistory(cartHistory: CartHistoryEntity)

    @Delete
    suspend fun deleteCartHistory(cartHistory: CartHistoryEntity)

    @Query("DELETE FROM cart_history WHERE userId = :userId")
    suspend fun deleteCartHistoryByUser(userId: Int)
}
