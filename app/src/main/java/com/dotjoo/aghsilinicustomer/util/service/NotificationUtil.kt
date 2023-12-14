package com.dotjoo.aghsilinicustomer.util.service

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.dotjoo.aghsilinicustomer.R
import com.dotjoo.aghsilinicustomer.ui.activity.MainActivity
import com.dotjoo.aghsilinicustomer.util.Constants.CHANNEL_ID
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class NotificationUtil @Inject constructor(@ApplicationContext val context: Context) {
    companion object

    fun sendNotification(response: NotificationItem) {
        sendNow(response.body, response)
    }

    fun sendNotification(notification: RemoteMessage.Notification) {
        sendNow(notification.title, notification.body)
    }

    fun sendNotification(data: Map<String?, String?>) {
        sendNow(data["title"], data["body"])
    }

    fun sendNow(title: String?, body: String?) {
        val notificationBuilder = getNotificationBuilder(null)
            .setContentTitle(title)
            .setContentText(body)
        notify(notificationBuilder)
    }

    private fun sendNow(body: String, response: NotificationItem) {
        val notificationBuilder = getNotificationBuilder(response)
            .setContentText(body)
        notify(notificationBuilder)
    }

    private fun getNotificationBuilder(response: NotificationItem?): NotificationCompat.Builder {
        val builder = NotificationCompat.Builder(context, CHANNEL_ID).apply {
            setSmallIcon(R.drawable.logo_more_screen)
                .setAutoCancel(true).priority = NotificationCompat.PRIORITY_DEFAULT
        }
        val intent = Intent()
        intent.setClass(context, MainActivity::class.java)
        intent.putExtra(FCMService.NOTIFICATION_ITEM, response)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        setPendingIntent(intent, builder)
        return builder
    }

    private fun setPendingIntent(intent: Intent, builder: NotificationCompat.Builder) {
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            context,
            System.currentTimeMillis().toInt(),  //*Request code*/,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
        builder.setContentIntent(pendingIntent)
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
             val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, "name", importance)
            // Register the channel with the system
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

  /*  private fun createNotificationChannel( ) {

        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val serviceChannel = NotificationChannel(
            Constants.CHANNEL_ID, Constants.CHANNEL_Name,
           importance
        )
        manager!!.createNotificationChannel(serviceChannel)

    }
 */


    private fun notify(notificationBuilder: NotificationCompat.Builder) {
        // Since android Oreo notification channel is needed.
        createNotificationChannel()
        with(NotificationManagerCompat.from(context)) {
            // notificationId is a unique int for each notification that you must define
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            notify(1, notificationBuilder.build())
        }
    }

}