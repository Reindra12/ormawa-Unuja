package com.reindrairawan.organisasimahasiswa.infra.utils

import android.content.Context
import android.content.SharedPreferences

@Suppress("UNCHECKED_CAST")
class SharedPrefs(private val context: Context) {
    companion object {
        private const val PREF = "Ormawa"
        private const val PREF_TOKEN = "user_Token"
        private const val PREF_USERNAME = "username"

    }

    private val sharedPref: SharedPreferences =
        context.getSharedPreferences(PREF, Context.MODE_PRIVATE)

    fun saveUsername(username: String){
        put(PREF_USERNAME, username)
    }

    fun getUsername(): String{
        return get(PREF_USERNAME, String::class.java)
    }

    fun saveToken(token: String) {
        put(PREF_TOKEN, token)

    }

    fun getToken(): String {
        return get(PREF_TOKEN, String::class.java)
    }

    fun clear() {
        sharedPref.edit().run {
            remove(PREF_TOKEN)
        }.apply()

    }

    private fun <T> get(key: String, clazz: Class<T>): T =
        when (clazz) {
            String::class.java -> sharedPref.getString(key, "")
            Boolean::class.java -> sharedPref.getBoolean(key, false)
            Float::class.java -> sharedPref.getFloat(key, -1f)
            Double::class.java -> sharedPref.getFloat(key, -1f)
            Int::class.java -> sharedPref.getInt(key, -1)
            Long::class.java -> sharedPref.getLong(key, -1L)
            else -> null
        } as T

    private fun <T> put(key: String, data: T) {
        val editor = sharedPref.edit()

        when (data) {
            is String -> editor.putString(key, data)
            is Boolean -> editor.putBoolean(key, data)
            is Float -> editor.putFloat(key, data)
            is Double -> editor.putFloat(key, data.toFloat())
            is Long -> editor.putLong(key, data)
            is Int -> editor.putInt(key, data)
        }
        editor.apply()
    }
}