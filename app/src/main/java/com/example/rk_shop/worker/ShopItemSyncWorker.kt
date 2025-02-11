package com.example.rk_shop.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.example.rk_shop.data.network.ApiService
import com.example.rk_shop.data.network.NetworkResult
import com.example.rk_shop.data.repository.ShopItemRepository
import com.example.rk_shop.data.repository.UserRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.concurrent.TimeUnit

@HiltWorker
class ShopItemSyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    apiService: ApiService,
    userRepository: UserRepository,
    shopItemRepository: ShopItemRepository
) : BaseWorker(context, params, apiService, userRepository, shopItemRepository) {

    companion object {
        private const val WORK_NAME = "shop_item_sync_worker"
        private const val SYNC_INTERVAL_HOURS = 4L

        fun schedule(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(true)
                .build()

            val request = PeriodicWorkRequestBuilder<ShopItemSyncWorker>(
                SYNC_INTERVAL_HOURS, TimeUnit.HOURS
            )
                .setConstraints(constraints)
                .setBackoffCriteria(
                    BackoffPolicy.EXPONENTIAL,
                    WorkRequest.MIN_BACKOFF_MILLIS,
                    TimeUnit.MILLISECONDS
                )
                .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                request
            )
        }

        fun scheduleOneTime(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val request = OneTimeWorkRequestBuilder<ShopItemSyncWorker>()
                .setConstraints(constraints)
                .setBackoffCriteria(
                    BackoffPolicy.LINEAR,
                    WorkRequest.MIN_BACKOFF_MILLIS,
                    TimeUnit.MILLISECONDS
                )
                .build()

            WorkManager.getInstance(context).enqueueUniqueWork(
                "${WORK_NAME}_onetime",
                ExistingWorkPolicy.REPLACE,
                request
            )
        }

        fun cancel(context: Context) {
            WorkManager.getInstance(context).cancelUniqueWork(WORK_NAME)
        }
    }

    override suspend fun executeWork(): WorkResult {
        return try {
            // Get the last sync timestamp from input data or preferences
            val lastSyncTime = inputData.getLong("last_sync_time", 0)

            // Fetch updates from the server
            when (val result = shopItemRepository.refreshShopItems()) {
                is NetworkResult.Success -> {
                    // Update last sync time
                    val outputData = Data.Builder()
                        .putLong("last_sync_time", System.currentTimeMillis())
                        .build()
                    setProgress(outputData)

                    WorkResult.Success("Successfully synced shop items")
                }
                is NetworkResult.Error -> {
                    val error = result.message ?: "Failed to sync shop items"
                    if (shouldRetry(error)) {
                        // Return retry for specific errors
                        return WorkResult.Failure("Retrying sync: $error")
                    }
                    WorkResult.Failure(error)
                }
                NetworkResult.Loading -> WorkResult.Failure("Unexpected loading state")
            }
        } catch (e: Exception) {
            WorkResult.Failure("Sync failed: ${e.message}")
        }
    }

    private fun shouldRetry(error: String): Boolean {
        // Define conditions for retry based on error message
        return error.contains("timeout", ignoreCase = true) ||
                error.contains("connection", ignoreCase = true) ||
                error.contains("unavailable", ignoreCase = true)
    }

    override fun requiresAuthentication(): Boolean = true
}
