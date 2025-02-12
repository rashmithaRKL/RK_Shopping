package com.example.rk_shop.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.WorkerParameters
import com.example.rk_shop.data.network.ApiService
import com.example.rk_shop.data.repository.ShopItemRepository
import com.example.rk_shop.data.repository.UserRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class ShopSyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    apiService: ApiService,
    userRepository: UserRepository,
    shopItemRepository: ShopItemRepository
) : BaseWorker(context, params, apiService, userRepository, shopItemRepository) {

    override suspend fun executeWork(): WorkResult {
        return try {
            // Sync shop items
            shopItemRepository.refreshShopItems()
            WorkResult.Success("Shop items synchronized successfully")
        } catch (e: Exception) {
            WorkResult.Failure(e.message ?: "Failed to sync shop items")
        }
    }

    override fun requiresAuthentication(): Boolean = true
}
