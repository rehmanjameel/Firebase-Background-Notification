package com.example.firebasebackgroundnotification

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.lang.Exception

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class MessagingServiceFirebase: FirebaseMessagingService() {

    lateinit var sharedPreferences: SharedPreferences

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        sharedPreferences = getSharedPreferences("UserNumber", Context.MODE_PRIVATE)

        val number = sharedPreferences.getInt("Number", 0)
        Log.e("Numbers", number.toString())
//        sendNotification()

        try {
            //check if message contains a data payload.
            remoteMessage.data.isNotEmpty().let {
                if (!remoteMessage.data.isNullOrEmpty()) {
                    Log.e("Number", number.toString())
                    val msg: String = remoteMessage.data["value"]!!

                    if (msg == number.toString()) {
                        sendNotification()
                    }
                    println("Value getting ${remoteMessage.data["value"]}")
                }
            }
        } catch (e: Exception) {
            Log.e("ExceptionError", e.toString())
        }

    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun sendNotification() {

        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        //Building FirebasePush notification
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notifications)
            .setContentTitle("FCM_message")
            .setContentText("message")
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Channel human readable title", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(0, notificationBuilder.build())
    }
}