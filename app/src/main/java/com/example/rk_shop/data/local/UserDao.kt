package com.example.rk_shop.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.rk_shop.data.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE id = :userId")
    fun getUserById(userId: String): Flow<User?>

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    fun getUserByEmail(email: String): Flow<User?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    @Query("DELETE FROM users")
    suspend fun deleteAllUsers()

    @Query("SELECT * FROM users WHERE id = :userId")
    fun observeUser(userId: String): LiveData<User>

    @Transaction
    suspend fun upsertUser(user: User) {
        val existingUser = getUserById(user.id)
        if (existingUser != null) {
            updateUser(user)
        } else {
            insertUser(user)
        }
    }

    @Query("SELECT EXISTS(SELECT 1 FROM users WHERE email = :email)")
    suspend fun isEmailTaken(email: String): Boolean

    @Query("SELECT * FROM users WHERE email = :email AND id != :excludeUserId")
    suspend fun findDuplicateEmail(email: String, excludeUserId: String): User?
}
