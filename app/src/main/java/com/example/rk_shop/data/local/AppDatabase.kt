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
            .fallbackToDestructiveMigration()
            .build()
        }
    }
}

// Type converters for Room
@androidx.room.TypeConverters
class Converters {
    @androidx.room.TypeConverter
    fun fromString(value: String): List<String> {
        return value.split(",").map { it.trim() }
    }

    @androidx.room.TypeConverter
    fun toString(list: List<String>): String {
        return list.joinToString(",")
    }

    @androidx.room.TypeConverter
    fun fromStringMap(value: String): Map<String, String> {
        if (value.isEmpty()) return emptyMap()
        return value.split(",").associate {
            val (key, value) = it.split(":")
            key to value
        }
    }

    @androidx.room.TypeConverter
    fun toStringMap(map: Map<String, String>): String {
        return map.entries.joinToString(",") { "${it.key}:${it.value}" }
    }

    @androidx.room.TypeConverter
    fun fromBigDecimal(value: String): java.math.BigDecimal {
        return java.math.BigDecimal(value)
    }

    @androidx.room.TypeConverter
    fun toBigDecimal(bigDecimal: java.math.BigDecimal): String {
        return bigDecimal.toString()
    }
}
