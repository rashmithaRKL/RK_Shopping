package com.example.rk_shop.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.rk_shop.data.model.ShopItem
import com.example.rk_shop.data.model.User

@Database(
    entities = [
        User::class,
        ShopItem::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun shopItemDao(): ShopItemDao

    companion object {
        private const val DATABASE_NAME = "rk_shop_db"

        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                DATABASE_NAME
            )
            .addTypeConverter(Converters())
            .fallbackToDestructiveMigration()
            .build()
        }
    }
}
