import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.rk_shop.data.model.ShopItem
import com.example.rk_shop.data.model.User
import com.example.rk_shop.data.model.Review
import com.example.rk_shop.data.model.Order
import com.example.rk_shop.data.local.UserDao
import com.example.rk_shop.data.local.ShopItemDao
import com.example.rk_shop.data.local.ReviewDao
import com.example.rk_shop.data.local.OrderDao
import android.content.Context

@Database(entities = [User::class, ShopItem::class, Review::class, Order::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun shopItemDao(): ShopItemDao
    abstract fun reviewDao(): ReviewDao
    abstract fun orderDao(): OrderDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "rk_shopping_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
