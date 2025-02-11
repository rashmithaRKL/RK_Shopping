package com.example.rk_shop.service

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import com.example.rk_shop.MainActivity
import com.example.rk_shop.R
import com.example.rk_shop.RKShopApplication
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@AndroidEntryPoint
class FCMService : FirebaseMessagingService() {

    @Inject
    lateinit var notificationManager: NotificationManager

    private val serviceScope = CoroutineScope(Dispatchers.IO)

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // Send the new token to your server
        serviceScope.launch {
            sendRegistrationTokenToServer(token)
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // Handle data payload
        remoteMessage.data.isNotEmpty().let {
            handleDataMessage(remoteMessage.data)
        }

        // Handle notification payload
        remoteMessage.notification?.let {
            handleNotificationMessage(it)
        }
    }

    private fun handleDataMessage(data: Map<String, String>) {
        val type = data["type"]
        when (type) {
            "order_update" -> handleOrderUpdate(data)
            "promotion" -> handlePromotion(data)
            "chat_message" -> handleChatMessage(data)
            else -> handleDefaultMessage(data)
        }
    }

    private fun handleNotificationMessage(notification: RemoteMessage.Notification) {
        val title = notification.title ?: getString(R.string.app_name)
        val message = notification.body ?: return

        showNotification(
            title,
            message,
            RKShopApplication.NOTIFICATION_CHANNEL_ID
        )
    }

    private fun handleOrderUpdate(data: Map<String, String>) {
        val orderId = data["order_id"]
        val status = data["status"]
        val message = data["message"]

        if (message != null) {
            showNotification(
                getString(R.string.order_status_notification_title),
                message,
                "order_updates"
            )
        }
    }

    private fun handlePromotion(data: Map<String, String>) {
        val title = data["title"]
        val message = data["message"]

        if (title != null && message != null) {
            showNotification(
                title,
                message,
                "promotions"
            )
        }
    }

    private fun handleChatMessage(data: Map<String, String>) {
        val sender = data["sender"]
        val message = data["message"]

        if (sender != null && message != null) {
            showNotification(
                sender,
                message,
                "chat"
            )
        }
    }

    private fun handleDefaultMessage(data: Map<String, String>) {
        val title = data["title"]
        val message = data["message"]

        if (title != null && message != null) {
            showNotification(
                title,
                message,
                RKShopApplication.NOTIFICATION_CHANNEL_ID
            )
        }
    }

    private fun showNotification(
        title: String,
        message: String,
        channelId: String
    ) {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        notificationManager.notify(
            Random.nextInt(),
            notificationBuilder.build()
        )
    }

    private suspend fun sendRegistrationTokenToServer(token: String) {
        try {
            // Implement the API call to send the token to your server
            // Example:
            // apiService.updateFcmToken(token)
        } catch (e: Exception) {
            // Handle error
        }
    }

    companion object {
        private const val TAG = "FCMService"
    }
}
