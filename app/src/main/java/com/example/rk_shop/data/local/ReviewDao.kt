package com.example.rk_shop.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.rk_shop.data.model.Review
import kotlinx.coroutines.flow.Flow

@Dao
interface ReviewDao {
    @Query("SELECT * FROM reviews")
    fun getAllReviews(): Flow<List<Review>>

    @Query("SELECT * FROM reviews WHERE productId = :productId")
    fun getReviewsForProduct(productId: String): Flow<List<Review>>

    @Query("SELECT * FROM reviews WHERE id = :id")
    suspend fun getReviewById(id: Long): Review?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReview(review: Review): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReviews(reviews: List<Review>)

    @Update
    suspend fun updateReview(review: Review)

    @Delete
    suspend fun deleteReview(review: Review)

    @Query("DELETE FROM reviews WHERE productId = :productId")
    suspend fun deleteReviewsForProduct(productId: String)

    @Query("DELETE FROM reviews")
    suspend fun deleteAllReviews()
}
