package com.example.rk_shop.di

import android.content.Context
import com.example.rk_shop.data.local.AppDatabase
import com.example.rk_shop.data.local.ShopItemDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Provides
    fun provideShopItemDao(database: AppDatabase): ShopItemDao {
        return database.shopItemDao()
    }
}
