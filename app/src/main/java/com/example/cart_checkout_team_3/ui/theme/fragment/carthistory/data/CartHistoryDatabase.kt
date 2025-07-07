package com.example.cart_checkout_team_3.ui.theme.fragment.carthistory.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.cart_checkout_team_3.data.local.dao.CartHistoryDao

@Database(entities = [CartHistoryEntity::class], version = 1)
abstract class CartHistoryDatabase : RoomDatabase() {
    abstract fun cartHistoryDao(): CartHistoryDao

    companion object {
        @Volatile
        private var INSTANCE: CartHistoryDatabase? = null

        fun getDatabase(context: Context): CartHistoryDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CartHistoryDatabase::class.java,
                    "cart_history_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
