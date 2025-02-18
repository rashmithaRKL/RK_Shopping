package com.example.rk_shop.di

import com.example.rk_shop.data.remote.DataSource
import com.example.rk_shop.data.remote.FirebaseDataSource
import com.example.rk_shop.data.remote.MySqlDataSource
import com.example.rk_shop.data.repository.ShopRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Firebase

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MySQL

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Provides
    @Singleton
    @Firebase
    fun provideFirebaseDataSource(): DataSource {
        return FirebaseDataSource()
    }

    @Provides
    @Singleton
    @MySQL
    fun provideMySqlDataSource(): DataSource {
        return MySqlDataSource()
    }

    @Provides
    @Singleton
    fun provideShopRepository(
        @Firebase firebaseDataSource: DataSource,
        @MySQL mySqlDataSource: DataSource
    ): ShopRepository {
        return ShopRepository(firebaseDataSource, mySqlDataSource)
    }
}
