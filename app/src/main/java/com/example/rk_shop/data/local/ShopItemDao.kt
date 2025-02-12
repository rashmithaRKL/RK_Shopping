package com.example.rk_shop.data.local

import androidx.room.*
import com.example.rk_shop.data.model.ShopItem
import kotlinx.coroutines.flow.Flow

@Dao
interface ShopItemDao {
    @Query("SELECT * FROM shop_items")
    fun getAllItems(): Flow<List<ShopItem>>

    @Query("SELECT * FROM shop_items WHERE id = :id")
    suspend fun getItemById(id: String): ShopItem?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: ShopItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItems(items: List<ShopItem>)

    @Update
    suspend fun updateItem(item: ShopItem)

    @Delete
    suspend fun deleteItem(item: ShopItem)

    @Query("DELETE FROM shop_items")
    suspend fun deleteAllItems()

    @Query("SELECT * FROM shop_items WHERE category = :category")
    fun getItemsByCategory(category: String): Flow<List<ShopItem>>

    @Query("SELECT * FROM shop_items WHERE price <= :maxPrice")
    fun getItemsByMaxPrice(maxPrice: Double): Flow<List<ShopItem>>
}
