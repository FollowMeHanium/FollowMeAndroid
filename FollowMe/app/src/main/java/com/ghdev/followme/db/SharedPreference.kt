package com.ghdev.followme.db

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

object SharedPreference {
    private const val PREFS_KEY_ACCESS = "token"
    private const val PREFS_KEY_REF = "refreshToken"
    private lateinit var preferences : SharedPreferences

    fun init(context: Context) {
        preferences = context.getSharedPreferences(context.packageName, Activity.MODE_PRIVATE)}

}