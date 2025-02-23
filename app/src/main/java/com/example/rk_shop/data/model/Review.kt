package com.example.rk_shop.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.rk_shop.data.local.Converters
import java.util.Date

@Entity(tableName = "reviews")
@TypeConverters(Converters::class)
data class Review(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val productId: String,
    val userId: String,
    val rating: Float,
    val comment: String,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date()
)
