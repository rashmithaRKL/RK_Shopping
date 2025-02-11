package com.example.rk_shop.data.network

import com.example.rk_shop.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val BASE_URL = "https://api.rkshop.com/" // Replace with your actual API base URL
    private const val TIMEOUT = 30L

    private val okHttpClient by lazy {
        OkHttpClient.Builder().apply {
            if (BuildConfig.DEBUG) {
                addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
            }
            addInterceptor { chain ->
                val original = chain.request()
                val requestBuilder = original.newBuilder()
                    .header("Content-Type", "application/json")
                    .method(original.method, original.body)
                
                // Add auth token if available
                getStoredToken()?.let { token ->
                    requestBuilder.header("Authorization", "Bearer $token")
                }
                
                chain.proceed(requestBuilder.build())
            }
            connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            readTimeout(TIMEOUT, TimeUnit.SECONDS)
            writeTimeout(TIMEOUT, TimeUnit.SECONDS)
        }.build()
    }

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

    private fun getStoredToken(): String? {
        // Implement token retrieval from SharedPreferences or other secure storage
        return null // Replace with actual implementation
    }

    fun updateBaseUrl(newUrl: String) {
        // This method can be used to dynamically change the base URL if needed
        // Note: You would need to recreate the Retrofit instance
    }
}

// Extension function to handle API responses
suspend fun <T> safeApiCall(apiCall: suspend () -> retrofit2.Response<T>): NetworkResult<T> {
    return try {
        val response = apiCall()
        if (response.isSuccessful) {
            NetworkResult.Success(response.body()!!)
        } else {
            NetworkResult.Error(
                code = response.code(),
                message = response.message(),
                errorBody = response.errorBody()?.string()
            )
        }
    } catch (e: Exception) {
        NetworkResult.Error(message = e.message ?: "An error occurred")
    }
}

sealed class NetworkResult<out T> {
    data class Success<out T>(val data: T) : NetworkResult<T>()
    data class Error(
        val code: Int? = null,
        val message: String? = null,
        val errorBody: String? = null
    ) : NetworkResult<Nothing>()
    object Loading : NetworkResult<Nothing>()
}
