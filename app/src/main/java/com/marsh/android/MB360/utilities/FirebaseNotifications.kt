package com.marsh.android.MB360.utilities

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.marsh.android.MB360.insurance.DashBoardActivity

class FirebaseNotifications : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        LogMyBenefits.d("FCM TOKEN", "Refreshed token: $token")

    }

    override fun onMessageReceived(message: RemoteMessage) {
        LogMyBenefits.d("FIREBASE NOTIFICATIONS", message.from.toString())
        if (message.data.isNotEmpty()) {
            LogMyBenefits.d("FIREBASE NOTIFICATIONS", "SIZE: " + message.data)
        }

        if (message.notification != null) {
            LogMyBenefits.d("FIREBASE NOTIFICATIONS", message.notification!!.body)
            //sendNotification(message.notification!!.title, message.notification!!.body)
        }
    }


    private fun sendNotification(title: String?, message: String?) {
        val intent = Intent(this, DashBoardActivity::class.java)
        intent.putExtra("SCREEN", "ECARD")
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_UPDATE_CURRENT)

        val channelId = "server_notification"

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder: NotificationCompat.Builder = NotificationCompat.Builder(this, channelId)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(channelId,
                "Benefits You India Channel",
                NotificationManager.IMPORTANCE_HIGH)
        notificationManager.createNotificationChannel(channel)

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }
}