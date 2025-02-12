package com.example.rk_shop.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.example.rk_shop.data.network.ApiService
import com.example.rk_shop.data.repository.ShopItemRepository
import com.example.rk_shop.data.repository.UserRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class BaseWorker constructor(
    context: Context,
    params: WorkerParameters,
    protected val apiService: ApiService,
    protected val userRepository: UserRepository,
    protected val shopItemRepository: ShopItemRepository
) : CoroutineWorker(context, params) {

    companion object {
        const val KEY_ERROR_MESSAGE = "error_message"
        const val KEY_RESULT_MESSAGE = "result_message"
        const val KEY_IS_SUCCESS = "is_success"
    }

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            // Check network connectivity
            if (!isNetworkAvailable()) {
                return@withContext createFailureResult("No network connection available")
            }

            // Check authentication if required
            if (requiresAuthentication() && !isUserAuthenticated()) {
                return@withContext createFailureResult("Authentication required")
            }

            // Execute the work
            val result = executeWork()

            // Handle the result
            when (result) {
                is WorkResult.Success -> createSuccessResult(result.message)
                is WorkResult.Failure -> createFailureResult(result.error)
            }
        } catch (e: Exception) {
            createFailureResult(e.message ?: "Unknown error occurred")
        }
    }

    /**
     * Execute the actual work. This should be implemented by concrete worker classes.
     */
    protected abstract suspend fun executeWork(): WorkResult

    /**
     * Override this to specify if the worker requires authentication
     */
    protected open fun requiresAuthentication(): Boolean = false

    /**
     * Check if user is authenticated
     */
    private fun isUserAuthenticated(): Boolean {
        return userRepository.isUserLoggedIn()
    }

    /**
     * Check network connectivity
     */
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as android.net.ConnectivityManager
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        return capabilities != null &&
                (capabilities.hasTransport(android.net.NetworkCapabilities.TRANSPORT_WIFI) ||
                        capabilities.hasTransport(android.net.NetworkCapabilities.TRANSPORT_CELLULAR) ||
                        capabilities.hasTransport(android.net.NetworkCapabilities.TRANSPORT_ETHERNET))
    }

    /**
     * Create success result with output data
     */
    protected fun createSuccessResult(message: String? = null): Result {
        val outputData = Data.Builder()
            .putBoolean(KEY_IS_SUCCESS, true)
            .putString(KEY_RESULT_MESSAGE, message)
            .build()
        return Result.success(outputData)
    }

    /**
     * Create failure result with error message
     */
    protected fun createFailureResult(error: String): Result {
        val outputData = Data.Builder()
            .putBoolean(KEY_IS_SUCCESS, false)
            .putString(KEY_ERROR_MESSAGE, error)
            .build()
        return Result.failure(outputData)
    }

    /**
     * Create retry result with error message
     */
    protected fun createRetryResult(error: String): Result {
        val outputData = Data.Builder()
            .putBoolean(KEY_IS_SUCCESS, false)
            .putString(KEY_ERROR_MESSAGE, error)
            .build()
        return Result.retry()
    }
}

sealed class WorkResult {
    data class Success(val message: String? = null) : WorkResult()
    data class Failure(val error: String) : WorkResult()
}
