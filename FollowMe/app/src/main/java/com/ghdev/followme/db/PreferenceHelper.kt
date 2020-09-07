package com.ghdev.followme.db

import android.content.Context
import android.content.SharedPreferences

//SharedPreference 에서 쓰기/읽기를 관리하는 클래스
class PreferenceHelper(context: Context) {

    companion object {
        private const val PREFS_FILENAME = "token_data"
        private const val PREFS_KEY_ACCESS = "token"
        private const val PREFS_KEY_REF = "refreshToken"
    }

    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)

    fun getString(key: String, defValue:String):String{
        return prefs.getString(key, defValue).toString()
    }

    fun setString(key: String, str:String){
        prefs.edit().putString(key, str).apply()
    }

}