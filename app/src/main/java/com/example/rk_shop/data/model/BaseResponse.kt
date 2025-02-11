package com.example.rk_shop.data.model

sealed class BaseResponse<out T> {
    data class Success<out T>(val data: T) : BaseResponse<T>()
    data class Error(val message: String, val code: Int? = null) : BaseResponse<Nothing>()
    object Loading : BaseResponse<Nothing>()
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

data class ApiResponse<T>(
    val status: Boolean,
    val message: String,
    val data: T?
)
