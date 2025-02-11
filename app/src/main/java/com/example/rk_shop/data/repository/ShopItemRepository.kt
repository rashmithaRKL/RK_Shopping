package com.example.rk_shop.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.rk_shop.data.local.ShopItemDao
import com.example.rk_shop.data.model.NetworkResult
import com.example.rk_shop.data.model.ShopItem
import com.example.rk_shop.data.network.ApiService
import com.example.rk_shop.data.network.safeApiCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShopItemRepository @Inject constructor(
    private val shopItemDao: ShopItemDao,
    private val apiService: ApiService
) {
    companion object {
        private const val PAGE_SIZE = 20
    }

    // Remote data operations
    suspend fun refreshShopItems() {
        withContext(Dispatchers.IO) {
            try {
                val response = safeApiCall { 
                    apiService.getShopItems(page = 1, limit = 100)
                }
                when (response) {
                    is NetworkResult.Success -> {
                        response.data.data?.let { items ->
                            shopItemDao.deleteAllItems()
                            shopItemDao.insertItems(items)
                        }
                    }
                    else -> { /* Handle error cases */ }
                }
            } catch (e: Exception) {
                // Handle exceptions
            }
        }
    }

    suspend fun createShopItem(item: ShopItem): NetworkResult<ShopItem> {
        return withContext(Dispatchers.IO) {
            val response = safeApiCall { apiService.createShopItem(item) }
            when (response) {
                is NetworkResult.Success -> {
                    response.data.data?.let { newItem ->
                        shopItemDao.insertItem(newItem)
                        NetworkResult.Success(newItem)
                    } ?: NetworkResult.Error(message = "Failed to create item")
                }
                is NetworkResult.Error -> response
                NetworkResult.Loading -> NetworkResult.Loading
            }
        }
    }

    suspend fun updateShopItem(item: ShopItem): NetworkResult<ShopItem> {
        return withContext(Dispatchers.IO) {
            val response = safeApiCall { apiService.updateShopItem(item.id, item) }
            when (response) {
                is NetworkResult.Success -> {
                    response.data.data?.let { updatedItem ->
                        shopItemDao.updateItem(updatedItem)
                        NetworkResult.Success(updatedItem)
                    } ?: NetworkResult.Error(message = "Failed to update item")
                }
                is NetworkResult.Error -> response
                NetworkResult.Loading -> NetworkResult.Loading
            }
        }
    }

    suspend fun deleteShopItem(itemId: String): NetworkResult<Unit> {
        return withContext(Dispatchers.IO) {
            val response = safeApiCall { apiService.deleteShopItem(itemId) }
            when (response) {
                is NetworkResult.Success -> {
                    shopItemDao.deleteItem(ShopItem(id = itemId, name = "", description = "", 
                        price = java.math.BigDecimal.ZERO, category = "", createdAt = "", updatedAt = "", stock = 0))
                    NetworkResult.Success(Unit)
                }
                is NetworkResult.Error -> response
                NetworkResult.Loading -> NetworkResult.Loading
            }
        }
    }

    // Local data operations
    fun getPagedShopItems(): Flow<PagingData<ShopItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { shopItemDao.getPagedItems() }
        ).flow
    }

    fun getAllItems(): Flow<List<ShopItem>> = shopItemDao.getAllItems()

    fun getItemById(itemId: String): Flow<ShopItem?> = shopItemDao.getItemById(itemId)

    fun getItemsByCategory(category: String): Flow<List<ShopItem>> = 
        shopItemDao.getItemsByCategory(category)

    fun searchItems(query: String): Flow<List<ShopItem>> = shopItemDao.searchItems(query)

    fun getAvailableItems(): Flow<List<ShopItem>> = shopItemDao.getAvailableItems()

    fun getTopRatedItems(minRating: Float = 4.0f): Flow<List<ShopItem>> = 
        shopItemDao.getTopRatedItems(minRating)

    fun getDiscountedItems(): Flow<List<ShopItem>> = shopItemDao.getDiscountedItems()

    suspend fun updateStock(itemId: String, newStock: Int) {
        withContext(Dispatchers.IO) {
            shopItemDao.updateStock(itemId, newStock)
            // Optionally sync with backend
            val item = shopItemDao.getItemById(itemId).value
            item?.let { updateShopItem(it.copy(stock = newStock)) }
        }
    }

    // Network bound resource
    fun getShopItemsWithRefresh(): Flow<NetworkResult<List<ShopItem>>> = flow {
        emit(NetworkResult.Loading)
        
        // Emit cached data first
        val cachedItems = shopItemDao.getAllItems().value
        cachedItems?.let { emit(NetworkResult.Success(it)) }

        // Fetch fresh data from network
        try {
            val response = safeApiCall { apiService.getShopItems(page = 1, limit = 100) }
            when (response) {
                is NetworkResult.Success -> {
                    response.data.data?.let { items ->
                        shopItemDao.deleteAllItems()
                        shopItemDao.insertItems(items)
                        emit(NetworkResult.Success(items))
                    }
                }
                is NetworkResult.Error -> emit(response)
                NetworkResult.Loading -> emit(NetworkResult.Loading)
            }
        } catch (e: Exception) {
            emit(NetworkResult.Error(message = e.message ?: "Failed to fetch items"))
        }
    }.flowOn(Dispatchers.IO)
}
