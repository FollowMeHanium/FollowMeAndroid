package com.ghdev.followme.db

import android.content.Context
import android.content.SharedPreferences

//SharedPreference 에서 쓰기/읽기를 관리하는 클래스
class PreferenceHelper(context: Context) {

    companion object {
        private const val PREFS_FILENAME = "token_data"
        //access
        public const val PREFS_KEY_ACCESS = "token"
        //refresh
        public const val PREFS_KEY_REF = "refreshToken"
        //user email
        public const val PREFS_KEY_EMAIL = "email"
        //user password
        public const val PREFS_KEY_PASSWORD = "password"
    }

    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)

    var token : String = ""
    var refreshToken : String =""

    fun getString(key: String, defValue:String):String{
        return prefs.getString(key, defValue).toString()
    }

    fun setString(key: String, str:String){
        prefs.edit().putString(key, str).apply()
    }

}