package com.dotjoo.aghsilinicustomer.data

import android.content.Context
import android.content.SharedPreferences
import com.dotjoo.aghsilinicustomer.data.remote.response.LoginResponse
import com.dotjoo.aghsilinicustomer.data.remote.response.UserResponse
import com.dotjoo.aghsilinicustomer.util.Constants
import com.google.gson.Gson

object PrefsHelper {

    private lateinit var preferences: SharedPreferences
    private const val PREFS_NAME = "shared_prefs"

    fun init(context: Context){
      preferences = context.getSharedPreferences(
          PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun setLanguage(language: String) {
      preferences.edit().putString(Constants.LANG, language).apply()
    }

    fun getLanguage(): String {
        return preferences.getString(Constants.LANG, Constants.AR).toString()
    }


    fun saveToken(token: String?) {
      preferences.edit().putString(Constants.TOKEN, token).apply()

    }


    fun getToken(): String {
        return preferences.getString(Constants.TOKEN, "").toString()
    }

    fun setLoggedInBefore(B: Boolean) {
        preferences.edit().putBoolean(Constants.isLoggedInBefore, B).apply()

    }

    fun getLoggedinBefore(): Boolean {
        return preferences.getBoolean(Constants.isLoggedInBefore, false)
    }



    fun clear()  {
       saveUserData(null)
      preferences.edit().putString(Constants.TOKEN, "").apply()
     }

    fun setFCMToken(token: String) {
      preferences.edit().putString(Constants.FCM_TOKEN, token).apply()
    }
    fun getFcmToken(): String {
        return preferences.getString(Constants.FCM_TOKEN,"").toString()
    }

    fun saveUserData(user: UserResponse?) {
        //set variables of 'myObject', etc.

        var prefsEditor = preferences.edit()
        var gson = Gson()
        //  String
        var json = gson.toJson(user);
        prefsEditor.putString(Constants.USER, json);
        prefsEditor.commit();
    }

    fun getUserData(): UserResponse? {
        //set variables of 'myObject', etc.

        val gson = Gson()
        val json: String? = preferences.getString(Constants.USER, "")
        return gson.fromJson(json, UserResponse::class.java)
    }

}