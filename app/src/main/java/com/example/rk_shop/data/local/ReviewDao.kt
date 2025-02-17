import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.rk_shop.data.model.Review

@Dao
interface ReviewDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(review: Review)

    @Query("SELECT * FROM reviews WHERE id = :reviewId")
    suspend fun getReviewById(reviewId: String): Review?

    @Query("SELECT * FROM reviews WHERE shopItemId = :itemId")
    suspend fun getReviewsForShopItem(itemId: String): List<Review>

    @Query("DELETE FROM reviews WHERE id = :reviewId")
    suspend fun deleteReviewById(reviewId: String)
}
