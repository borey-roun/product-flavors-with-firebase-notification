package com.example.productflavors

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        message.notification?.apply {
            val resultIntent = Intent(
                this@MyFirebaseMessagingService, MainActivity::class.java
            )
            val resultPendingIntent: PendingIntent? =
                TaskStackBuilder.create(this@MyFirebaseMessagingService).run {
                    // Add the intent, which inflates the back stack
                    addNextIntentWithParentStack(resultIntent)
                    // Get the PendingIntent containing the entire back stack
                    getPendingIntent(
                        0, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )
                }
            val builder = NotificationCompat.Builder(this@MyFirebaseMessagingService, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground).setContentTitle(title).setContentText(body)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setLargeIcon(BitmapFactory.decodeResource(resources,R.drawable.ic_launcher_foreground))
                .setContentIntent(resultPendingIntent)
                .setAutoCancel(true)
                .setStyle(NotificationCompat.DecoratedCustomViewStyle())

            createNotificationChannel().notify((0..100).random(), builder.build())
        }

    }

    private var CHANNEL_ID = BuildConfig.APPLICATION_ID

    private fun createNotificationChannel(): NotificationManager {
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.app_name)
            val descriptionText = getString(R.string.app_name)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            notificationManager.createNotificationChannel(channel)
        }
        return notificationManager
    }
}