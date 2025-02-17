import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.rk_shop.data.model.ShopItem

@Dao
interface ShopItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(shopItem: ShopItem)

    @Query("SELECT * FROM shop_items WHERE id = :itemId")
    suspend fun getShopItemById(itemId: String): ShopItem?

    @Query("SELECT * FROM shop_items")
    suspend fun getAllShopItems(): List<ShopItem>

    @Query("DELETE FROM shop_items WHERE id = :itemId")
    suspend fun deleteShopItemById(itemId: String)
}
