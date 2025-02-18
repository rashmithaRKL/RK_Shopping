package com.example.rk_shop.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rk_shop.data.model.*
import com.example.rk_shop.data.repository.ShopRepository
import com.example.rk_shop.data.repository.ShopRepository.SyncStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ShopViewModel @Inject constructor(
    private val repository: ShopRepository
) : ViewModel() {

    // UI State
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    // Shop Items
    val shopItems = repository.getShopItemsFlow()

    // Sync Status
    val syncStatus = repository.syncStatus

    // Cart Items
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems = _cartItems.asStateFlow()

    // User
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser = _currentUser.asStateFlow()

    init {
        refreshShopItems()
    }

    fun refreshShopItems() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                repository.refreshShopItems()
                    .onSuccess {
                        _uiState.value = UiState.Success
                    }
                    .onFailure { error ->
                        _uiState.value = UiState.Error(error.message ?: "Failed to load shop items")
                    }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                repository.login(email, password)
                    .onSuccess { user ->
                        _currentUser.value = user
                        _uiState.value = UiState.Success
                    }
                    .onFailure { error ->
                        _uiState.value = UiState.Error(error.message ?: "Login failed")
                    }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val newUser = User(
                    id = UUID.randomUUID().toString(),
                    name = name,
                    email = email,
                    passwordHash = "", // Will be set by repository
                    phone = "",
                    address = ""
                )
                
                repository.createUser(newUser, password)
                    .onSuccess { user ->
                        _currentUser.value = user
                        _uiState.value = UiState.Success
                    }
                    .onFailure { error ->
                        _uiState.value = UiState.Error(error.message ?: "Registration failed")
                    }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    fun addToCart(shopItem: ShopItem, quantity: Int = 1) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val currentUser = _currentUser.value ?: throw IllegalStateException("User not logged in")
                
                val cartItem = CartItem(
                    id = UUID.randomUUID().toString(),
                    userId = currentUser.id,
                    productId = shopItem.id,
                    quantity = quantity
                )

                repository.addToCart(cartItem)
                    .onSuccess {
                        _uiState.value = UiState.Success
                        refreshCartItems(currentUser.id)
                    }
                    .onFailure { error ->
                        _uiState.value = UiState.Error(error.message ?: "Failed to add to cart")
                    }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    private fun refreshCartItems(userId: String) {
        viewModelScope.launch {
            try {
                repository.getCartItems(userId)
                    .onSuccess { items ->
                        _cartItems.value = items
                    }
            } catch (e: Exception) {
                // Handle error silently as this is a background refresh
            }
        }
    }

    fun createOrder(shippingAddress: String, paymentMethod: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val currentUser = _currentUser.value ?: throw IllegalStateException("User not logged in")
                val cartItems = _cartItems.value
                
                if (cartItems.isEmpty()) {
                    throw IllegalStateException("Cart is empty")
                }

                val order = Order(
                    id = UUID.randomUUID().toString(),
                    userId = currentUser.id,
                    items = cartItems,
                    totalAmount = calculateTotal(cartItems),
                    status = "PENDING",
                    shippingAddress = shippingAddress,
                    paymentMethod = paymentMethod
                )

                repository.createOrder(order)
                    .onSuccess {
                        _uiState.value = UiState.Success
                        refreshCartItems(currentUser.id)
                    }
                    .onFailure { error ->
                        _uiState.value = UiState.Error(error.message ?: "Failed to create order")
                    }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    private fun calculateTotal(cartItems: List<CartItem>): BigDecimal {
        return cartItems.fold(BigDecimal.ZERO) { acc, item ->
            val shopItem = shopItems.value.find { it.id == item.productId }
            shopItem?.let {
                acc + (it.price * BigDecimal(item.quantity))
            } ?: acc
        }
    }

    sealed class UiState {
        object Loading : UiState()
        object Success : UiState()
        data class Error(val message: String) : UiState()
    }
}
