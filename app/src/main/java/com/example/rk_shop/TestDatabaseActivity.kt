package com.example.rk_shop

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.rk_shop.data.model.ShopItem
import com.example.rk_shop.data.model.User
import com.example.rk_shop.data.remote.MySqlConfig
import com.example.rk_shop.ui.viewmodel.ShopViewModel
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import java.util.UUID

@AndroidEntryPoint
class TestDatabaseActivity : AppCompatActivity() {
    private val TAG = "TestDatabaseActivity"
    private val viewModel: ShopViewModel by viewModels()
    
    private lateinit var statusTextView: TextView
    private lateinit var testMySqlButton: Button
    private lateinit var testFirebaseButton: Button
    private lateinit var testSyncButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_database)

        statusTextView = findViewById(R.id.statusTextView)
        testMySqlButton = findViewById(R.id.testMySqlButton)
        testFirebaseButton = findViewById(R.id.testFirebaseButton)
        testSyncButton = findViewById(R.id.testSyncButton)

        setupClickListeners()
        observeViewModel()
    }

    private fun setupClickListeners() {
        testMySqlButton.setOnClickListener {
            testMySqlConnection()
        }

        testFirebaseButton.setOnClickListener {
            testFirebaseConnection()
        }

        testSyncButton.setOnClickListener {
            testDataSync()
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.syncStatus.collect { status ->
                updateStatus("Sync Status: $status")
            }
        }
    }

    private fun testMySqlConnection() {
        lifecycleScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    val connection = MySqlConfig.getConnection()
                    if (connection.isValid(5)) {
                        // Test basic CRUD operations
                        val stmt = connection.createStatement()
                        
                        // Test SELECT
                        val rs = stmt.executeQuery("SELECT COUNT(*) FROM products")
                        rs.next()
                        val productCount = rs.getInt(1)
                        
                        updateStatus("MySQL Connection Successful\nProduct Count: $productCount")
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "MySQL test failed", e)
                updateStatus("MySQL Test Failed: ${e.message}")
            }
        }
    }

    private fun testFirebaseConnection() {
        lifecycleScope.launch {
            try {
                val db = FirebaseFirestore.getInstance()
                val testDoc = hashMapOf("test" to "test_${System.currentTimeMillis()}")
                
                // Test write
                db.collection("test")
                    .document("test")
                    .set(testDoc)
                    .await()

                // Test read
                val snapshot = db.collection("test")
                    .document("test")
                    .get()
                    .await()

                if (snapshot.exists()) {
                    updateStatus("Firebase Connection Successful\nTest Document: ${snapshot.data}")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Firebase test failed", e)
                updateStatus("Firebase Test Failed: ${e.message}")
            }
        }
    }

    private fun testDataSync() {
        lifecycleScope.launch {
            try {
                // Create test user
                val testUser = User(
                    id = UUID.randomUUID().toString(),
                    email = "test_${System.currentTimeMillis()}@test.com",
                    passwordHash = "test_hash",
                    name = "Test User",
                    phone = "1234567890",
                    address = "Test Address"
                )

                // Create test product
                val testProduct = ShopItem(
                    id = UUID.randomUUID().toString(),
                    name = "Test Product",
                    description = "Test Description",
                    price = BigDecimal("9.99"),
                    imageUrl = "test_image.jpg",
                    stockQuantity = 10
                )

                // Test registration
                viewModel.register(testUser.name, testUser.email, "test_password")
                
                // Add product to cart
                viewModel.addToCart(testProduct)

                updateStatus("Data Sync Test Started\nCheck logs for details")

            } catch (e: Exception) {
                Log.e(TAG, "Data sync test failed", e)
                updateStatus("Data Sync Test Failed: ${e.message}")
            }
        }
    }

    private fun updateStatus(message: String) {
        lifecycleScope.launch(Dispatchers.Main) {
            statusTextView.text = message
            Log.d(TAG, message)
        }
    }
}
