package com.example.cart_checkout_team_3.ui.theme.fragment.carthistory.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_history")
data class CartHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val summary: String,
    val total: Int,
    val date: String
)
