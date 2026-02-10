package dev.esbi.mizan.core.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dev.esbi.mizan.MainActivity
import dev.esbi.mizan.R

/**
 * Firebase Cloud Messaging Service for handling push notifications.
 *
 * This service handles:
 * - New FCM token registration
 * - Incoming push notification messages
 * - Foreground notification display
 */
class MizanFirebaseMessagingService : FirebaseMessagingService() {

    companion object {
        private const val TAG = "MizanFCMService"
        private const val CHANNEL_ID = "mizan_notifications"
        private const val CHANNEL_NAME = "Mizan Notifications"
        private const val CHANNEL_DESCRIPTION = "Notifications from Mizan app"
    }

    /**
     * Called when a new FCM registration token is generated.
     * This can happen when:
     * - The app is installed for the first time
     * - The user clears app data
     * - The app is restored on a new device
     *
     * @param token The new FCM registration token
     */
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "New FCM Token: $token")
        
        // TODO: Send token to your backend server for push notification targeting
        sendTokenToServer(token)
    }

    /**
     * Called when a message is received from FCM.
     *
     * @param remoteMessage The received message from Firebase
     */
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        
        Log.d(TAG, "Message received from: ${remoteMessage.from}")

        // Check if message contains a data payload
        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: ${remoteMessage.data}")
            handleDataMessage(remoteMessage.data)
        }

        // Check if message contains a notification payload
        remoteMessage.notification?.let { notification ->
            Log.d(TAG, "Message notification body: ${notification.body}")
            showNotification(
                title = notification.title ?: getString(R.string.app_name),
                body = notification.body ?: ""
            )
        }
    }

    /**
     * Handle data-only messages (silent push notifications).
     * These can be used for background data sync or other operations.
     *
     * @param data The data payload from the message
     */
    private fun handleDataMessage(data: Map<String, String>) {
        val title = data["title"] ?: getString(R.string.app_name)
        val body = data["body"] ?: data["message"] ?: ""
        val type = data["type"]

        when (type) {
            "transaction" -> {
                // Handle transaction-related notifications
                Log.d(TAG, "Transaction notification received")
            }
            "budget_alert" -> {
                // Handle budget alert notifications
                Log.d(TAG, "Budget alert notification received")
            }
            "reminder" -> {
                // Handle reminder notifications
                Log.d(TAG, "Reminder notification received")
            }
            else -> {
                // Default handling
                Log.d(TAG, "Generic notification received")
            }
        }

        // Show notification if there's content to display
        if (body.isNotEmpty()) {
            showNotification(title, body)
        }
    }

    /**
     * Display a local system notification.
     * Creates notification channel for Android O+ and shows the notification.
     *
     * @param title The notification title
     * @param body The notification body/message
     */
    private fun showNotification(title: String, body: String) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create notification channel for Android O and above
        createNotificationChannel(notificationManager)

        // Create intent to open the app when notification is tapped
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Build the notification
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .build()

        // Show the notification with a unique ID based on current time
        val notificationId = System.currentTimeMillis().toInt()
        notificationManager.notify(notificationId, notification)
    }

    /**
     * Create notification channel for Android O and above.
     *
     * @param notificationManager The system notification manager
     */
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = CHANNEL_DESCRIPTION
                enableLights(true)
                enableVibration(true)
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    /**
     * Send the FCM token to your backend server.
     * TODO: Implement actual server communication.
     *
     * @param token The FCM registration token
     */
    private fun sendTokenToServer(token: String) {
        // TODO: Implement API call to send token to backend
        Log.d(TAG, "TODO: Send token to server: $token")
    }
}
