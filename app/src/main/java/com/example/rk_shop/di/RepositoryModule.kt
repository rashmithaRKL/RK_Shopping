package com.example.rk_shop.di

import android.content.SharedPreferences
import com.example.rk_shop.data.local.ShopItemDao
import com.example.rk_shop.data.local.UserDao
import com.example.rk_shop.data.network.ApiService
import com.example.rk_shop.data.repository.ShopItemRepository
import com.example.rk_shop.data.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideUserRepository(
        userDao: UserDao,
        apiService: ApiService,
        firebaseAuth: FirebaseAuth,
        sharedPreferences: SharedPreferences
    ): UserRepository {
        return UserRepository(
            userDao = userDao,
            apiService = apiService,
            firebaseAuth = firebaseAuth,
            sharedPreferences = sharedPreferences
        )
    }

    @Provides
    @Singleton
    fun provideShopItemRepository(
        shopItemDao: ShopItemDao,
        apiService: ApiService
    ): ShopItemRepository {
        return ShopItemRepository(
            shopItemDao = shopItemDao,
            apiService = apiService
        )
    }

    // Add other repository providers as needed
    /*
    @Provides
    @Singleton
    fun provideOrderRepository(
        orderDao: OrderDao,
        apiService: ApiService
    ): OrderRepository {
        return OrderRepository(
            orderDao = orderDao,
            apiService = apiService
        )
    }

    @Provides
    @Singleton
    fun provideCartRepository(
        cartDao: CartDao,
        apiService: ApiService
    ): CartRepository {
        return CartRepository(
            cartDao = cartDao,
            apiService = apiService
        )
    }
    */
}
