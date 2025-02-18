package com.example.rk_shop.data.remote

import com.example.rk_shop.data.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.sql.SQLException
import java.sql.Connection
import java.util.UUID

class MySqlDataSource : DataSource {
    private val connection get() = MySqlConfig.getConnection()

    override suspend fun createUser(user: User): Result<User> = withContext(Dispatchers.IO) {
        try {
            val stmt = connection.prepareStatement(
                """
                INSERT INTO users (id, email, password_hash, name, phone, address)
                VALUES (?, ?, ?, ?, ?, ?)
                """
            )
            stmt.setString(1, user.id)
            stmt.setString(2, user.email)
            stmt.setString(3, user.passwordHash)
            stmt.setString(4, user.name)
            stmt.setString(5, user.phone)
            stmt.setString(6, user.address)
            
            stmt.executeUpdate()
            Result.success(user)
        } catch (e: SQLException) {
            Result.failure(e)
        }
    }

    override suspend fun getUser(id: String): Result<User> = withContext(Dispatchers.IO) {
        try {
            val stmt = connection.prepareStatement("SELECT * FROM users WHERE id = ?")
            stmt.setString(1, id)
            val rs = stmt.executeQuery()
            
            if (rs.next()) {
                Result.success(User(
                    id = rs.getString("id"),
                    email = rs.getString("email"),
                    passwordHash = rs.getString("password_hash"),
                    name = rs.getString("name"),
                    phone = rs.getString("phone"),
                    address = rs.getString("address")
                ))
            } else {
                Result.failure(NoSuchElementException("User not found"))
            }
        } catch (e: SQLException) {
            Result.failure(e)
        }
    }

    override suspend fun getUserByEmail(email: String): Result<User> = withContext(Dispatchers.IO) {
        try {
            val stmt = connection.prepareStatement("SELECT * FROM users WHERE email = ?")
            stmt.setString(1, email)
            val rs = stmt.executeQuery()
            
            if (rs.next()) {
                Result.success(User(
                    id = rs.getString("id"),
                    email = rs.getString("email"),
                    passwordHash = rs.getString("password_hash"),
                    name = rs.getString("name"),
                    phone = rs.getString("phone"),
                    address = rs.getString("address")
                ))
            } else {
                Result.failure(NoSuchElementException("User not found"))
            }
        } catch (e: SQLException) {
            Result.failure(e)
        }
    }

    override suspend fun getShopItems(): Result<List<ShopItem>> = withContext(Dispatchers.IO) {
        try {
            val stmt = connection.prepareStatement("SELECT * FROM products")
            val rs = stmt.executeQuery()
            val items = mutableListOf<ShopItem>()
            
            while (rs.next()) {
                items.add(ShopItem(
                    id = rs.getString("id"),
                    name = rs.getString("name"),
                    description = rs.getString("description"),
                    price = rs.getBigDecimal("price"),
                    imageUrl = rs.getString("image_url"),
                    stockQuantity = rs.getInt("stock_quantity")
                ))
            }
            Result.success(items)
        } catch (e: SQLException) {
            Result.failure(e)
        }
    }

    override suspend fun addToCart(cartItem: CartItem): Result<CartItem> = withContext(Dispatchers.IO) {
        try {
            val stmt = connection.prepareStatement(
                """
                INSERT INTO cart_items (id, user_id, product_id, quantity)
                VALUES (?, ?, ?, ?)
                """
            )
            stmt.setString(1, UUID.randomUUID().toString())
            stmt.setString(2, cartItem.userId)
            stmt.setString(3, cartItem.productId)
            stmt.setInt(4, cartItem.quantity)
            
            stmt.executeUpdate()
            Result.success(cartItem)
        } catch (e: SQLException) {
            Result.failure(e)
        }
    }

    override suspend fun createOrder(order: Order): Result<Order> = withContext(Dispatchers.IO) {
        val connection = MySqlConfig.getConnection()
        connection.autoCommit = false
        
        try {
            // Insert order
            val orderStmt = connection.prepareStatement(
                """
                INSERT INTO orders (id, user_id, total_amount, status, shipping_address, payment_method)
                VALUES (?, ?, ?, ?, ?, ?)
                """
            )
            orderStmt.setString(1, order.id)
            orderStmt.setString(2, order.userId)
            orderStmt.setBigDecimal(3, order.totalAmount)
            orderStmt.setString(4, order.status)
            orderStmt.setString(5, order.shippingAddress)
            orderStmt.setString(6, order.paymentMethod)
            orderStmt.executeUpdate()

            // Insert order items
            val itemStmt = connection.prepareStatement(
                """
                INSERT INTO order_items (id, order_id, product_id, quantity, price_at_time)
                VALUES (?, ?, ?, ?, ?)
                """
            )
            order.items.forEach { item ->
                itemStmt.setString(1, UUID.randomUUID().toString())
                itemStmt.setString(2, order.id)
                itemStmt.setString(3, item.productId)
                itemStmt.setInt(4, item.quantity)
                itemStmt.setBigDecimal(5, item.price)
                itemStmt.addBatch()
            }
            itemStmt.executeBatch()

            connection.commit()
            Result.success(order)
        } catch (e: SQLException) {
            connection.rollback()
            Result.failure(e)
        } finally {
            connection.autoCommit = true
            connection.close()
        }
    }

    // Implement other DataSource methods similarly...

    override suspend fun login(email: String, password: String): Result<User> = 
        getUserByEmail(email).map { user ->
            if (verifyPassword(password, user.passwordHash)) {
                user
            } else {
                throw IllegalArgumentException("Invalid password")
            }
        }

    override suspend fun register(user: User, password: String): Result<User> {
        val hashedPassword = hashPassword(password)
        return createUser(user.copy(passwordHash = hashedPassword))
    }

    private fun hashPassword(password: String): String {
        // Implement proper password hashing (e.g., using BCrypt)
        return password // Placeholder implementation
    }

    private fun verifyPassword(password: String, hashedPassword: String): Boolean {
        // Implement proper password verification
        return password == hashedPassword // Placeholder implementation
    }
}
