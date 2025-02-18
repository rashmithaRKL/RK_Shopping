package com.example.rk_shop.data.repository

import com.example.rk_shop.data.model.*
import com.example.rk_shop.data.remote.DataSource
import com.example.rk_shop.di.Firebase
import com.example.rk_shop.di.MySQL
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShopRepository @Inject constructor(
    @Firebase private val firebaseDataSource: DataSource,
    @MySQL private val mySqlDataSource: DataSource
) {
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    
    private val _syncStatus = MutableStateFlow<SyncStatus>(SyncStatus.Idle)
    val syncStatus = _syncStatus.asStateFlow()

    // User operations with data synchronization
    suspend fun createUser(user: User, password: String): Result<User> {
        return try {
            _syncStatus.value = SyncStatus.Syncing
            
            // First create in MySQL
            val mysqlResult = mySqlDataSource.register(user, password)
            if (mysqlResult.isSuccess) {
                // Then create in Firebase
                val firebaseResult = firebaseDataSource.register(mysqlResult.getOrThrow(), password)
                _syncStatus.value = if (firebaseResult.isSuccess) {
                    SyncStatus.Success
                } else {
                    SyncStatus.Error("Firebase sync failed")
                }
                mysqlResult
            } else {
                _syncStatus.value = SyncStatus.Error("MySQL registration failed")
                mysqlResult
            }
        } catch (e: Exception) {
            _syncStatus.value = SyncStatus.Error(e.message ?: "Unknown error")
            Result.failure(e)
        }
    }

    suspend fun login(email: String, password: String): Result<User> {
        return try {
            // First authenticate with Firebase
            val firebaseResult = firebaseDataSource.login(email, password)
            if (firebaseResult.isSuccess) {
                // Then verify with MySQL
                val mysqlResult = mySqlDataSource.login(email, password)
                if (mysqlResult.isSuccess) {
                    // Start background sync of user data
                    syncUserData(mysqlResult.getOrThrow())
                }
                mysqlResult
            } else {
                firebaseResult
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun syncUserData(user: User) {
        coroutineScope.launch {
            try {
                // Sync cart items
                val cartItems = mySqlDataSource.getCartItems(user.id).getOrNull() ?: emptyList()
                cartItems.forEach { item ->
                    firebaseDataSource.addToCart(item)
                }

                // Sync wishlist items
                val wishlistItems = mySqlDataSource.getWishlistItems(user.id).getOrNull() ?: emptyList()
                wishlistItems.forEach { item ->
                    firebaseDataSource.addToWishlist(item)
                }

                // Sync orders
                val orders = mySqlDataSource.getUserOrders(user.id).getOrNull() ?: emptyList()
                orders.forEach { order ->
                    firebaseDataSource.createOrder(order)
                }
            } catch (e: Exception) {
                _syncStatus.value = SyncStatus.Error("Background sync failed: ${e.message}")
            }
        }
    }

    // Shop item operations with caching
    private val shopItemsCache = MutableStateFlow<List<ShopItem>>(emptyList())

    fun getShopItemsFlow(): Flow<List<ShopItem>> = shopItemsCache.asStateFlow()

    suspend fun refreshShopItems(): Result<List<ShopItem>> {
        return try {
            _syncStatus.value = SyncStatus.Syncing
            
            // Get from MySQL as primary source
            val mysqlResult = mySqlDataSource.getShopItems()
            if (mysqlResult.isSuccess) {
                val items = mysqlResult.getOrThrow()
                shopItemsCache.value = items

                // Sync with Firebase in background
                coroutineScope.launch {
                    try {
                        items.forEach { item ->
                            firebaseDataSource.createShopItem(item)
                        }
                        _syncStatus.value = SyncStatus.Success
                    } catch (e: Exception) {
                        _syncStatus.value = SyncStatus.Error("Firebase sync failed: ${e.message}")
                    }
                }
                mysqlResult
            } else {
                // Fallback to Firebase
                val firebaseResult = firebaseDataSource.getShopItems()
                if (firebaseResult.isSuccess) {
                    shopItemsCache.value = firebaseResult.getOrThrow()
                }
                _syncStatus.value = if (firebaseResult.isSuccess) {
                    SyncStatus.Success
                } else {
                    SyncStatus.Error("Failed to fetch shop items")
                }
                firebaseResult
            }
        } catch (e: Exception) {
            _syncStatus.value = SyncStatus.Error(e.message ?: "Unknown error")
            Result.failure(e)
        }
    }

    // Cart operations with real-time updates
    suspend fun addToCart(cartItem: CartItem): Result<CartItem> {
        return try {
            _syncStatus.value = SyncStatus.Syncing
            
            // Add to MySQL first
            val mysqlResult = mySqlDataSource.addToCart(cartItem)
            if (mysqlResult.isSuccess) {
                // Sync with Firebase
                coroutineScope.launch {
                    try {
                        firebaseDataSource.addToCart(mysqlResult.getOrThrow())
                        _syncStatus.value = SyncStatus.Success
                    } catch (e: Exception) {
                        _syncStatus.value = SyncStatus.Error("Firebase sync failed: ${e.message}")
                    }
                }
                mysqlResult
            } else {
                _syncStatus.value = SyncStatus.Error("Failed to add to cart")
                mysqlResult
            }
        } catch (e: Exception) {
            _syncStatus.value = SyncStatus.Error(e.message ?: "Unknown error")
            Result.failure(e)
        }
    }

    // Order processing with transaction support
    suspend fun createOrder(order: Order): Result<Order> {
        return try {
            _syncStatus.value = SyncStatus.Syncing
            
            // Create in MySQL first
            val mysqlResult = mySqlDataSource.createOrder(order)
            if (mysqlResult.isSuccess) {
                // Sync with Firebase
                coroutineScope.launch {
                    try {
                        firebaseDataSource.createOrder(mysqlResult.getOrThrow())
                        _syncStatus.value = SyncStatus.Success
                    } catch (e: Exception) {
                        _syncStatus.value = SyncStatus.Error("Firebase sync failed: ${e.message}")
                    }
                }
                mysqlResult
            } else {
                _syncStatus.value = SyncStatus.Error("Failed to create order")
                mysqlResult
            }
        } catch (e: Exception) {
            _syncStatus.value = SyncStatus.Error(e.message ?: "Unknown error")
            Result.failure(e)
        }
    }

    // Data synchronization status
    sealed class SyncStatus {
        object Idle : SyncStatus()
        object Syncing : SyncStatus()
        object Success : SyncStatus()
        data class Error(val message: String) : SyncStatus()
    }
}
