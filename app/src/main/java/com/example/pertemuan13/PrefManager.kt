package com.example.pertemuan13

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

class PrefManager private constructor(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREFS_FILENAME = "AuthAppPrefs"
        private const val KEY_IS_LOGGED_IN = "isLoggedIn"
        private const val KEY_USERNAME = "username"

        @Volatile
        private var instance: PrefManager? = null
        fun getInstance(context: Context): PrefManager {
            return instance ?: synchronized(this) {
                instance ?: PrefManager(context.applicationContext).also {
                    instance = it
                }
            }
        }
    }

    fun setLoggedIn(isLoggedIn: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn)
        editor.apply()
    }

    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun saveUsername(username: String) {
        val editor = sharedPreferences.edit()
        editor.putString(KEY_USERNAME, username)
        editor.apply()
    }

    fun getusername(): String {
        return sharedPreferences.getString(KEY_USERNAME, "").orEmpty()
    }

    fun clear() {
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()

        // Log untuk memastikan data dihapus
        val isLoggedIn = sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
        val username = sharedPreferences.getString(KEY_USERNAME, null)
        Log.d("PrefManager", "Data cleared: isLoggedIn=$isLoggedIn, username=$username")
    }
}
