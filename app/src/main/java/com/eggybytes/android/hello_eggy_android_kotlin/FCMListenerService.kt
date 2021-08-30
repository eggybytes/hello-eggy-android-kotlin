package com.eggybytes.android.hello_eggy_android_kotlin

import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import com.eggybytes.android.eggy_android.EggyClient
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FCMListenerService : FirebaseMessagingService() {
    // Called if InstanceID token is updated. This may occur if the security of the previous token
    // has been compromised. This is also called when the InstanceID token is initially generated
    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")

        Thread {
            EggyClient.registerWithDeviceApi("", token)
        }.start()
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "From: ${remoteMessage.from}")

        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: ${remoteMessage.data}")
        }

        // Check if message contains a notification payload, and if there is one, display it
        remoteMessage.notification?.let {
            Log.d(TAG, "Message notification: $it")
            Log.d(TAG, "Message notification title: ${it.title}")
            Log.d(TAG, "Message notification body: ${it.body}")

            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channelId = getString(R.string.default_notification_channel_id)

            val notification = NotificationCompat.Builder(this, channelId)
                .setContentTitle(it.title)
                .setContentText(it.body)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .build()
            manager.notify(123, notification)
        }

        Thread {
            EggyClient.sendPushEvent("push_notification_was_received_foreground", remoteMessage)
        }.start()
    }
}

private const val TAG = "FCMListenerService"
