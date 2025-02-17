package com.example.rk_shop

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.rk_shop.data.local.AppDatabase
import com.example.rk_shop.data.model.User
import kotlinx.coroutines.launch
import java.util.UUID

class TestDatabaseActivity : AppCompatActivity() {

    private lateinit var database: AppDatabase
    private lateinit var tvStatus: TextView
    private lateinit var btnTestDatabase: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_database)

        // Initialize UI components
        tvStatus = findViewById(R.id.tvStatus)
        btnTestDatabase = findViewById(R.id.btnTestDatabase)

        // Initialize database
        database = AppDatabase.getDatabase(this)

        // Set up click listener
        btnTestDatabase.setOnClickListener {
            testDatabaseOperations()
        }
    }

    private fun testDatabaseOperations() {
        lifecycleScope.launch {
            try {
                // Create a test user
                val userId = UUID.randomUUID().toString()
                val user = User(
                    id = userId,
                    email = "test@example.com",
                    name = "Test User",
                    createdAt = System.currentTimeMillis().toString(),
                    updatedAt = System.currentTimeMillis().toString()
                )

                // Insert user
                database.userDao().insert(user)
                updateStatus("User inserted successfully")

                // Retrieve user
                val retrievedUser = database.userDao().getUserById(userId)
                if (retrievedUser != null) {
                    updateStatus("User retrieved successfully: ${retrievedUser.name}")
                } else {
                    updateStatus("Error: User not found")
                }

                // Delete user
                database.userDao().deleteUserById(userId)
                updateStatus("User deleted successfully")

            } catch (e: Exception) {
                Log.e(TAG, "Error during database operations", e)
                updateStatus("Error: ${e.message}")
            }
        }
    }

    private fun updateStatus(message: String) {
        val currentText = tvStatus.text.toString()
        val newText = if (currentText == "Database Test Status") {
            message
        } else {
            "$currentText\n$message"
        }
        tvStatus.text = newText
    }

    companion object {
        private const val TAG = "TestDatabaseActivity"
    }
}
