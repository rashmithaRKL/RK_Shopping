package com.example.rk_shop.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

@Entity(tableName = "shop_items")
@androidx.room.TypeConverters(com.example.rk_shop.data.local.Converters::class)
data class ShopItem(
    @PrimaryKey
    @SerializedName("id")
    val id: String,
    
    @SerializedName("name")
    val name: String,
    
    @SerializedName("description")
    val description: String,
    
    @SerializedName("price")
    val price: BigDecimal,
    
    @SerializedName("discountPrice")
    val discountPrice: BigDecimal? = null,
    
    @SerializedName("imageUrl")
    val imageUrl: String? = null,
    
    @SerializedName("category")
    val category: String,
    
    @SerializedName("tags")
    val tags: List<String> = emptyList(),
    
    @SerializedName("stock")
    val stock: Int,
    
    @SerializedName("rating")
    val rating: Float = 0f,
    
    @SerializedName("reviews")
    val reviews: List<Review> = emptyList(),
    
    @SerializedName("specifications")
    val specifications: Map<String, String> = emptyMap(),
    
    @SerializedName("createdAt")
    val createdAt: String,
    
    @SerializedName("updatedAt")
    val updatedAt: String
)

@androidx.room.TypeConverters(Converters::class)
data class Review(
    @SerializedName("id")
    val id: String,
    
    @SerializedName("userId")
    val userId: String,
    
    @SerializedName("userName")
    val userName: String,
    
    @SerializedName("rating")
    val rating: Float,
    
    @SerializedName("comment")
    val comment: String,
    
    @SerializedName("createdAt")
    val createdAt: String
)

data class CartItem(
    @SerializedName("itemId")
    val itemId: String,
    
    @SerializedName("quantity")
    val quantity: Int,
    
    @SerializedName("price")
    val price: BigDecimal,
    
    @SerializedName("totalPrice")
    val totalPrice: BigDecimal = price.multiply(BigDecimal(quantity))
)

data class Order(
    @SerializedName("id")
    val id: String,
    
    @SerializedName("userId")
    val userId: String,
    
    @SerializedName("items")
    val items: List<CartItem>,
    
    @SerializedName("totalAmount")
    val totalAmount: BigDecimal,
    
    @SerializedName("status")
    val status: OrderStatus,
    
    @SerializedName("shippingAddress")
    val shippingAddress: String,
    
    @SerializedName("paymentMethod")
    val paymentMethod: PaymentMethod,
    
    @SerializedName("createdAt")
    val createdAt: String,
    
    @SerializedName("updatedAt")
    val updatedAt: String
)

enum class OrderStatus {
    PENDING,
    CONFIRMED,
    PROCESSING,
    SHIPPED,
    DELIVERED,
    CANCELLED
}

enum class PaymentMethod {
    CREDIT_CARD,
    DEBIT_CARD,
    PAYPAL,
    CASH_ON_DELIVERY
}

data class Category(
    @SerializedName("id")
    val id: String,
    
    @SerializedName("name")
    val name: String,
    
    @SerializedName("description")
    val description: String? = null,
    
    @SerializedName("imageUrl")
    val imageUrl: String? = null,
    
    @SerializedName("parentCategoryId")
    val parentCategoryId: String? = null
)
