package com.dotjoo.aghsilinicustomer.util.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.dotjoo.aghsilinicustomer.ui.activity.MainActivity
import com.dotjoo.aghsilinicustomer.util.Constants

class FcmBroadcast : BroadcastReceiver() {


    override fun onReceive(context: Context, intent: Intent) {
        val mainIntent = Intent()
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val response: Int? = intent.getIntExtra(Constants.Notifaction,-1)

          if (intent.action != null) {
            if (intent.action.equals(MainActivity.MAIN_SCREEN_ACTION)) {
                mainIntent.setClass(context, MainActivity::class.java)
            }
            context.startActivity(mainIntent)
        }
    }
}