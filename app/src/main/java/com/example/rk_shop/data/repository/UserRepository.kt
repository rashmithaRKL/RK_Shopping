package com.example.rk_shop.data.repository

import android.content.SharedPreferences
import com.example.rk_shop.data.local.UserDao
import com.example.rk_shop.data.model.AuthRequest
import com.example.rk_shop.data.model.NetworkResult
import com.example.rk_shop.data.model.User
import com.example.rk_shop.data.network.ApiService
import com.example.rk_shop.data.network.safeApiCall
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userDao: UserDao,
    private val apiService: ApiService,
    private val firebaseAuth: FirebaseAuth,
    private val sharedPreferences: SharedPreferences
) {
    companion object {
        private const val KEY_AUTH_TOKEN = "auth_token"
        private const val KEY_USER_ID = "user_id"
    }

    // Firebase Authentication
    suspend fun firebaseSignIn(email: String, password: String): NetworkResult<User> {
        return withContext(Dispatchers.IO) {
            try {
                val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
                result.user?.let { firebaseUser ->
                    // Get user details from backend
                    val response = safeApiCall {
                        apiService.login(mapOf(
                            "email" to email,
                            "password" to password
                        ))
                    }
                    
                    when (response) {
                        is NetworkResult.Success -> {
                            val user = response.data.data!!
                            // Cache user locally
                            userDao.insertUser(user)
                            // Save auth token
                            saveAuthToken(response.data.data.id)
                            NetworkResult.Success(user)
                        }
                        is NetworkResult.Error -> NetworkResult.Error(
                            message = response.message ?: "Login failed"
                        )
                        NetworkResult.Loading -> NetworkResult.Loading
                    }
                } ?: NetworkResult.Error(message = "Firebase authentication failed")
            } catch (e: Exception) {
                NetworkResult.Error(message = e.message ?: "Authentication failed")
            }
        }
    }

    suspend fun firebaseSignUp(email: String, password: String, name: String): NetworkResult<User> {
        return withContext(Dispatchers.IO) {
            try {
                val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
                result.user?.let { firebaseUser ->
                    // Register user in backend
                    val response = safeApiCall {
                        apiService.register(mapOf(
                            "email" to email,
                            "password" to password,
                            "name" to name
                        ))
                    }
                    
                    when (response) {
                        is NetworkResult.Success -> {
                            val user = response.data.data!!
                            userDao.insertUser(user)
                            saveAuthToken(response.data.data.id)
                            NetworkResult.Success(user)
                        }
                        is NetworkResult.Error -> NetworkResult.Error(
                            message = response.message ?: "Registration failed"
                        )
                        NetworkResult.Loading -> NetworkResult.Loading
                    }
                } ?: NetworkResult.Error(message = "Firebase registration failed")
            } catch (e: Exception) {
                NetworkResult.Error(message = e.message ?: "Registration failed")
            }
        }
    }

    suspend fun signOut() {
        withContext(Dispatchers.IO) {
            firebaseAuth.signOut()
            clearAuthToken()
            userDao.deleteAllUsers()
        }
    }

    // Local data operations
    fun getCurrentUser(): Flow<User?> {
        val userId = getUserId()
        return userDao.getUserById(userId)
    }

    suspend fun updateUserProfile(user: User): NetworkResult<User> {
        return withContext(Dispatchers.IO) {
            val response = safeApiCall {
                apiService.updateUserProfile(user)
            }
            
            when (response) {
                is NetworkResult.Success -> {
                    val updatedUser = response.data.data!!
                    userDao.updateUser(updatedUser)
                    NetworkResult.Success(updatedUser)
                }
                is NetworkResult.Error -> response
                NetworkResult.Loading -> NetworkResult.Loading
            }
        }
    }

    // Token management
    private fun saveAuthToken(token: String) {
        sharedPreferences.edit()
            .putString(KEY_AUTH_TOKEN, token)
            .putString(KEY_USER_ID, token)
            .apply()
    }

    private fun clearAuthToken() {
        sharedPreferences.edit()
            .remove(KEY_AUTH_TOKEN)
            .remove(KEY_USER_ID)
            .apply()
    }

    fun getAuthToken(): String? {
        return sharedPreferences.getString(KEY_AUTH_TOKEN, null)
    }

    private fun getUserId(): String {
        return sharedPreferences.getString(KEY_USER_ID, "") ?: ""
    }

    fun isUserLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null && getAuthToken() != null
    }
}
