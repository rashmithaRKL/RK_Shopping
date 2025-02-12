package com.example.rk_shop.data.repository

import com.example.rk_shop.data.local.ShopItemDao
import com.example.rk_shop.data.model.ShopItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShopItemRepository @Inject constructor(
    private val shopItemDao: ShopItemDao
) {
    fun getAllItems(): Flow<List<ShopItem>> = shopItemDao.getAllItems()

    suspend fun getItemById(id: String): ShopItem? = shopItemDao.getItemById(id)

    suspend fun insertItem(item: ShopItem) = shopItemDao.insertItem(item)

    suspend fun insertItems(items: List<ShopItem>) = shopItemDao.insertItems(items)

    suspend fun updateItem(item: ShopItem) = shopItemDao.updateItem(item)

    suspend fun deleteItem(item: ShopItem) = shopItemDao.deleteItem(item)

    suspend fun deleteAllItems() = shopItemDao.deleteAllItems()

    fun getItemsByCategory(category: String): Flow<List<ShopItem>> = 
        shopItemDao.getItemsByCategory(category)

    fun getItemsByMaxPrice(maxPrice: Double): Flow<List<ShopItem>> = 
        shopItemDao.getItemsByMaxPrice(maxPrice)

    suspend fun refreshShopItems() {
        // TODO: Implement network call to fetch items
        // For now, this is just a placeholder for the sync worker
    }
}
