package com.example.cart_checkout_team_3.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Date

@Entity(tableName = "cart_history")
@TypeConverters(Converters::class)
data class CartHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: Int,
    val products: List<CartItemEntity>,
    val total: Double,
    val discountedTotal: Double,
    val totalProducts: Int,
    val totalQuantity: Int,
    val timestamp: Date,
    val status: String // "completed", "pending", "cancelled"
)

data class CartItemEntity(
    val productId: Int,
    val title: String,
    val price: Double,
    val quantity: Int,
    val total: Double,
    val thumbnail: String
)

class Converters {
    @TypeConverter
    fun fromCartItemsList(value: List<CartItemEntity>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toCartItemsList(value: String): List<CartItemEntity> {
        val listType = object : TypeToken<List<CartItemEntity>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun toDate(timestamp: Long?): Date? {
        return timestamp?.let { Date(it) }
    }
}
