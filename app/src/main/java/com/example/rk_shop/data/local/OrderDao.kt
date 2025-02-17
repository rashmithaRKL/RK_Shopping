import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.rk_shop.data.model.Order

@Dao
interface OrderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(order: Order)

    @Query("SELECT * FROM orders WHERE id = :orderId")
    suspend fun getOrderById(orderId: String): Order?

    @Query("SELECT * FROM orders WHERE userId = :userId")
    suspend fun getOrdersForUser(userId: String): List<Order>

    @Query("DELETE FROM orders WHERE id = :orderId")
    suspend fun deleteOrderById(orderId: String)
}
