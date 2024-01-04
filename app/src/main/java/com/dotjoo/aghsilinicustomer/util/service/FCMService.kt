package com.dotjoo.aghsilinicustomer.util.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.AudioAttributes
 import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDeepLinkBuilder
import com.dotjoo.aghsilinicustomer.R
import com.dotjoo.aghsilinicustomer.data.PrefsHelper
import com.dotjoo.aghsilinicustomer.ui.activity.MainActivity
import com.dotjoo.aghsilinicustomer.util.Constants
import com.dotjoo.aghsilinicustomer.util.Constants.CHANNEL_ID
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*

import javax.inject.Inject

@AndroidEntryPoint
class FCMService : FirebaseMessagingService() {

    companion object {
        private const val TAG = "MyFirebaseMessagingServ"
        const val NOTIFICATION_ITEM = "NOTIFICATION_ITEM"
        //   const val REFRESH_CRYSTALS = "REFRESH_CRYSTALS"
    }


    @Inject
    lateinit var fcmUseCase: UpdateFcmUseCase
    override fun onNewToken(s: String) {
        super.onNewToken(s)
        PrefsHelper.setFCMToken(s)
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.
      //  fcmUseCase.sendFcmTokenToServer(params = FcmParam(s))
       /* viewModelScope.launch {
            try {
                useCaseFcm.invoke()
            } catch (e: Exception) {
                Log.i("updateFcm", "updateFcm: ${e.message}")
            }
        }*/
    }

    /**
     * Called if the FCM registration token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the
     * FCM registration token is initially generated so this is where you would retrieve the token.
     */

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ

        showNotification(remoteMessage.data, remoteMessage.notification)
        //  NotificationUtil(context = this).sendNotification(response)
        // Check if message contains a notification payload.
        remoteMessage.notification?.let {
            Log.d("Notification", "c" + " ${it.body}+ ${it.title}")
        }

    }


    private fun showNotification(
        remoteMessage: Map<String, String>, notification: RemoteMessage.Notification?
    ) {
        Log.d(TAG, "showNotification: $remoteMessage")

         val orderId = remoteMessage["order_id"]
         val status = remoteMessage["status"]

        val contentIntent: PendingIntent? = (if (status != null) {

            if (status == "2") {

                orderId?.let { sendRealTimeBroadcast(it) }
                NavDeepLinkBuilder(applicationContext).setComponentName(MainActivity::class.java)
                    .setGraph(R.navigation.main_nav).setDestination(R.id.orderInfoFragment)
                    .setArguments(    bundleOf(
                         Constants.ORDER_ID to orderId,
                     )
                    )
                    .createPendingIntent()

            } else {
                NavDeepLinkBuilder(applicationContext).setComponentName(MainActivity::class.java)
                    .setGraph(R.navigation.main_nav).setDestination(R.id.homeFragment)
                    .createPendingIntent() }

        } else {
          //  val intent = Intent(this, MainActivity::class.java)
           // PendingIntent.getActivity(this, 100, intent, PendingIntent.FLAG_IMMUTABLE)
            NavDeepLinkBuilder(applicationContext).setComponentName(MainActivity::class.java)
                .setGraph(R.navigation.main_nav).setDestination(R.id.homeFragment)
                .createPendingIntent()
        })


        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val pattern = longArrayOf(500, 500, 500, 500, 500, 500, 500, 500, 500)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }


        contentIntent?.let {

            val notification = NotificationCompat.Builder(this, CHANNEL_ID).setAutoCancel(true)
                .setSmallIcon(R.drawable.logo_more_screen).setContentTitle(notification?.title)
                .setContentText(notification?.body).setContentIntent(it )
                // .setSound(soundUri)
                .setVibrate(pattern)


            notificationManager.notify(Random().nextInt(), notification.build())
        }

      }



    private fun sendRealTimeBroadcast(orderId:String ) {
        val intent =
            Intent(MainActivity.MAIN_SCREEN_ACTION) //used to receive in intent filter when register the broadcast
        intent.putExtra(Constants.Notifaction, orderId)
        sendBroadcast(intent)

    }
@RequiresApi(Build.VERSION_CODES.O)
private fun createNotificationChannel(notificationManager: NotificationManager) {

    val channelName = "Delivery   Channel"
    val audioAttributes =
        AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .setUsage(AudioAttributes.USAGE_ALARM).build()
    val channel = NotificationChannel(
        CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_HIGH
    ).apply {
        description = "A notification from Delivery  App"
        enableLights(true)
        lightColor = Color.GREEN
    }
     notificationManager.createNotificationChannel(channel)
}



}
