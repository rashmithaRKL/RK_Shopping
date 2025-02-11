package com.example.rk_shop.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "users")
data class User(
    @PrimaryKey
    @SerializedName("id")
    val id: String,
    
    @SerializedName("email")
    val email: String,
    
    @SerializedName("name")
    val name: String,
    
    @SerializedName("phone")
    val phone: String? = null,
    
    @SerializedName("address")
    val address: String? = null,
    
    @SerializedName("profileImage")
    val profileImage: String? = null,
    
    @SerializedName("createdAt")
    val createdAt: String,
    
    @SerializedName("updatedAt")
    val updatedAt: String,
    
    @SerializedName("preferences")
    val preferences: UserPreferences? = null
)

data class UserPreferences(
    @SerializedName("darkMode")
    val darkMode: Boolean = false,
    
    @SerializedName("notificationsEnabled")
    val notificationsEnabled: Boolean = true,
    
    @SerializedName("language")
    val language: String = "en",
    
    @SerializedName("currency")
    val currency: String = "USD"
)

data class UserLocation(
    @SerializedName("latitude")
    val latitude: Double,
    
    @SerializedName("longitude")
    val longitude: Double,
    
    @SerializedName("address")
    val address: String? = null
)

data class AuthRequest(
    @SerializedName("email")
    val email: String,
    
    @SerializedName("password")
    val password: String
)

data class AuthResponse(
    @SerializedName("token")
    val token: String,
    
    @SerializedName("user")
    val user: User
)

data class UpdateProfileRequest(
    @SerializedName("name")
    val name: String? = null,
    
    @SerializedName("phone")
    val phone: String? = null,
    
    @SerializedName("address")
    val address: String? = null,
    
    @SerializedName("preferences")
    val preferences: UserPreferences? = null
)
