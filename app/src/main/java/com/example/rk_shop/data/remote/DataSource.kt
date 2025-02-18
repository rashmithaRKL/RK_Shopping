package com.example.rk_shop.data.remote

import com.example.rk_shop.data.model.User
import com.example.rk_shop.data.model.ShopItem
import com.example.rk_shop.data.model.Order
import com.example.rk_shop.data.model.CartItem
import com.example.rk_shop.data.model.WishlistItem

interface DataSource {
    // User operations
    suspend fun createUser(user: User): Result<User>
    suspend fun getUser(id: String): Result<User>
    suspend fun updateUser(user: User): Result<User>
    suspend fun deleteUser(id: String): Result<Boolean>
    suspend fun getUserByEmail(email: String): Result<User>

    // Shop item operations
    suspend fun getShopItems(): Result<List<ShopItem>>
    suspend fun getShopItem(id: String): Result<ShopItem>
    suspend fun createShopItem(item: ShopItem): Result<ShopItem>
    suspend fun updateShopItem(item: ShopItem): Result<ShopItem>
    suspend fun deleteShopItem(id: String): Result<Boolean>
    suspend fun searchShopItems(query: String): Result<List<ShopItem>>

    // Cart operations
    suspend fun addToCart(cartItem: CartItem): Result<CartItem>
    suspend fun removeFromCart(userId: String, itemId: String): Result<Boolean>
    suspend fun getCartItems(userId: String): Result<List<CartItem>>
    suspend fun updateCartItemQuantity(userId: String, itemId: String, quantity: Int): Result<Boolean>

    // Wishlist operations
    suspend fun addToWishlist(wishlistItem: WishlistItem): Result<WishlistItem>
    suspend fun removeFromWishlist(userId: String, itemId: String): Result<Boolean>
    suspend fun getWishlistItems(userId: String): Result<List<WishlistItem>>

    // Order operations
    suspend fun createOrder(order: Order): Result<Order>
    suspend fun getOrder(id: String): Result<Order>
    suspend fun getUserOrders(userId: String): Result<List<Order>>
    suspend fun updateOrderStatus(orderId: String, status: String): Result<Boolean>

    // Authentication
    suspend fun login(email: String, password: String): Result<User>
    suspend fun register(user: User, password: String): Result<User>
    suspend fun resetPassword(email: String): Result<Boolean>
}
