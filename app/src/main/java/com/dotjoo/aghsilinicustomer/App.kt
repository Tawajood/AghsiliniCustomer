package com.dotjoo.aghsilinicustomer

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.dotjoo.aghsilinicustomer.data.PrefsHelper
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App:Application() {



    /*
         override fun attachBaseContext(newBase: Context) {
            val locale = Locale(Constants.EN)
            val localeUpdatedContext: Context = updateLocale(newBase, locale)
             super.attachBaseContext(LocaleManager.setLocale(base));
             super.attachBaseContext(localeUpdatedContext)
        }*/

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

         PrefsHelper.init(applicationContext)

    }
}