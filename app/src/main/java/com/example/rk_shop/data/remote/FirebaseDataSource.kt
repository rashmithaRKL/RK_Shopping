package com.example.rk_shop.data.remote

import com.example.rk_shop.data.model.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Transaction
import kotlinx.coroutines.tasks.await
import java.util.UUID

class FirebaseDataSource : DataSource {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override suspend fun createUser(user: User): Result<User> = try {
        db.collection("users")
            .document(user.id)
            .set(user)
            .await()
        Result.success(user)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getUser(id: String): Result<User> = try {
        val snapshot = db.collection("users")
            .document(id)
            .get()
            .await()

        if (snapshot.exists()) {
            Result.success(snapshot.toObject(User::class.java)!!)
        } else {
            Result.failure(NoSuchElementException("User not found"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getUserByEmail(email: String): Result<User> = try {
        val snapshot = db.collection("users")
            .whereEqualTo("email", email)
            .get()
            .await()

        if (!snapshot.isEmpty) {
            Result.success(snapshot.documents[0].toObject(User::class.java)!!)
        } else {
            Result.failure(NoSuchElementException("User not found"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getShopItems(): Result<List<ShopItem>> = try {
        val snapshot = db.collection("products")
            .get()
            .await()

        Result.success(snapshot.toObjects(ShopItem::class.java))
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun addToCart(cartItem: CartItem): Result<CartItem> = try {
        db.collection("cart_items")
            .document(UUID.randomUUID().toString())
            .set(cartItem)
            .await()
        Result.success(cartItem)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun createOrder(order: Order): Result<Order> = try {
        db.runTransaction { transaction: Transaction ->
            // Create order document
            val orderRef = db.collection("orders").document(order.id)
            transaction.set(orderRef, order)

            // Create order items
            order.items.forEach { item ->
                val itemRef = db.collection("order_items").document(UUID.randomUUID().toString())
                transaction.set(itemRef, item)

                // Update product stock
                val productRef = db.collection("products").document(item.productId)
                val product = transaction.get(productRef).toObject(ShopItem::class.java)
                product?.let {
                    transaction.update(productRef, "stockQuantity", it.stockQuantity - item.quantity)
                }
            }

            // Clear cart items
            val cartSnapshot = db.collection("cart_items")
                .whereEqualTo("userId", order.userId)
                .get()
                .await()
            
            cartSnapshot.documents.forEach { doc ->
                transaction.delete(doc.reference)
            }
        }.await()

        Result.success(order)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun login(email: String, password: String): Result<User> = try {
        val authResult = auth.signInWithEmailAndPassword(email, password).await()
        authResult.user?.let { firebaseUser ->
            getUser(firebaseUser.uid)
        } ?: Result.failure(IllegalStateException("Authentication failed"))
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun register(user: User, password: String): Result<User> = try {
        val authResult = auth.createUserWithEmailAndPassword(user.email, password).await()
        authResult.user?.let { firebaseUser ->
            val newUser = user.copy(id = firebaseUser.uid)
            createUser(newUser).getOrThrow()
        } ?: throw IllegalStateException("User creation failed")
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun resetPassword(email: String): Result<Boolean> = try {
        auth.sendPasswordResetEmail(email).await()
        Result.success(true)
    } catch (e: Exception) {
        Result.failure(e)
    }

    // Implement other DataSource methods similarly...
}
