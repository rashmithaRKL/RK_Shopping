package com.example.rk_shop.data.local

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.*
import com.example.rk_shop.data.model.ShopItem
import kotlinx.coroutines.flow.Flow

@Dao
interface ShopItemDao {
    @Query("SELECT * FROM shop_items ORDER BY createdAt DESC")
    fun getAllItems(): Flow<List<ShopItem>>

    @Query("SELECT * FROM shop_items ORDER BY createdAt DESC")
    fun getPagedItems(): PagingSource<Int, ShopItem>

    @Query("SELECT * FROM shop_items WHERE id = :itemId")
    fun getItemById(itemId: String): Flow<ShopItem?>

    @Query("SELECT * FROM shop_items WHERE category = :category ORDER BY createdAt DESC")
    fun getItemsByCategory(category: String): Flow<List<ShopItem>>

    @Query("SELECT * FROM shop_items WHERE price <= :maxPrice ORDER BY price ASC")
    fun getItemsByPriceRange(maxPrice: Double): Flow<List<ShopItem>>

    @Query("SELECT * FROM shop_items WHERE name LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%'")
    fun searchItems(query: String): Flow<List<ShopItem>>

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

    @Query("SELECT * FROM shop_items WHERE id = :itemId")
    fun observeItem(itemId: String): LiveData<ShopItem>

    @Transaction
    suspend fun upsertItem(item: ShopItem) {
        val existingItem = getItemById(item.id)
        if (existingItem != null) {
            updateItem(item)
        } else {
            insertItem(item)
        }
    }

    @Query("SELECT * FROM shop_items WHERE stock > 0 ORDER BY createdAt DESC")
    fun getAvailableItems(): Flow<List<ShopItem>>

    @Query("SELECT DISTINCT category FROM shop_items")
    fun getAllCategories(): Flow<List<String>>

    @Query("SELECT * FROM shop_items WHERE rating >= :minRating ORDER BY rating DESC")
    fun getTopRatedItems(minRating: Float = 4.0f): Flow<List<ShopItem>>

    @Query("SELECT * FROM shop_items WHERE discountPrice IS NOT NULL ORDER BY (price - discountPrice) DESC")
    fun getDiscountedItems(): Flow<List<ShopItem>>

    @Query("SELECT COUNT(*) FROM shop_items")
    suspend fun getItemCount(): Int

    @Query("UPDATE shop_items SET stock = :newStock WHERE id = :itemId")
    suspend fun updateStock(itemId: String, newStock: Int)

    @Query("SELECT * FROM shop_items WHERE id IN (:itemIds)")
    fun getItemsByIds(itemIds: List<String>): Flow<List<ShopItem>>
}
