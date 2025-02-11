package com.example.rk_shop

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class RKShopApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "rk_shop_channel"
        private lateinit var instance: RKShopApplication

        fun getInstance(): RKShopApplication = instance
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        // Initialize Firebase
        FirebaseApp.initializeApp(this)
        FirebaseMessaging.getInstance().isAutoInitEnabled = true

        // Create notification channels
        createNotificationChannels()

        // Initialize other services as needed
        initializeServices()
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .setMinimumLoggingLevel(android.util.Log.INFO)
            .build()
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Main notification channel
            createNotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                getString(R.string.notification_channel_name),
                getString(R.string.notification_channel_description),
                NotificationManager.IMPORTANCE_DEFAULT
            )

            // Order updates channel
            createNotificationChannel(
                "order_updates",
                "Order Updates",
                "Notifications about your order status",
                NotificationManager.IMPORTANCE_HIGH
            )

            // Promotional channel
            createNotificationChannel(
                "promotions",
                "Promotions",
                "Special offers and promotions",
                NotificationManager.IMPORTANCE_LOW
            )
        }
    }

    private fun createNotificationChannel(
        channelId: String,
        name: String,
        description: String,
        importance: Int
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, name, importance).apply {
                this.description = description
            }
            
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun initializeServices() {
        // Subscribe to FCM topics
        FirebaseMessaging.getInstance().apply {
            subscribeToTopic("general")
            subscribeToTopic("promotions")
        }

        // Initialize crash reporting
        if (!BuildConfig.DEBUG) {
            // Initialize crash reporting services
            // Example: Crashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
        }

        // Initialize analytics
        // Example: Analytics.getInstance().setAnalyticsCollectionEnabled(true)

        // Initialize other third-party services
    }

    // Utility methods for the application scope
    fun getAppContext(): Context = applicationContext

    fun isDebugMode(): Boolean = BuildConfig.DEBUG

    // Add other application-wide utilities as needed
}
