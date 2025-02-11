package com.example.rk_shop.data.network

import com.example.rk_shop.data.model.ApiResponse
import com.example.rk_shop.data.model.ShopItem
import com.example.rk_shop.data.model.User
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    // Authentication
    @POST("auth/login")
    suspend fun login(
        @Body loginRequest: Map<String, String>
    ): Response<ApiResponse<User>>

    @POST("auth/register")
    suspend fun register(
        @Body registerRequest: Map<String, String>
    ): Response<ApiResponse<User>>

    // Shop Items
    @GET("items")
    suspend fun getShopItems(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Response<ApiResponse<List<ShopItem>>>

    @GET("items/{id}")
    suspend fun getShopItemById(
        @Path("id") id: String
    ): Response<ApiResponse<ShopItem>>

    @POST("items")
    suspend fun createShopItem(
        @Body item: ShopItem
    ): Response<ApiResponse<ShopItem>>

    @PUT("items/{id}")
    suspend fun updateShopItem(
        @Path("id") id: String,
        @Body item: ShopItem
    ): Response<ApiResponse<ShopItem>>

    @DELETE("items/{id}")
    suspend fun deleteShopItem(
        @Path("id") id: String
    ): Response<ApiResponse<Unit>>

    // User Profile
    @GET("users/profile")
    suspend fun getUserProfile(): Response<ApiResponse<User>>

    @PUT("users/profile")
    suspend fun updateUserProfile(
        @Body user: User
    ): Response<ApiResponse<User>>

    // Orders
    @POST("orders")
    suspend fun createOrder(
        @Body orderRequest: Map<String, Any>
    ): Response<ApiResponse<Any>>

    @GET("orders")
    suspend fun getOrders(): Response<ApiResponse<List<Any>>>

    // Location
    @POST("locations/nearby")
    suspend fun getNearbyShops(
        @Body location: Map<String, Double>
    ): Response<ApiResponse<List<Any>>>
}
