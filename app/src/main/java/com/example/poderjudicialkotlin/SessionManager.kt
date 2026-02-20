package com.example.poderjudicialkotlin

import android.content.Context

class SessionManager(context: Context) {

    private val prefs = context.getSharedPreferences("pj_session", Context.MODE_PRIVATE)

    fun setLoggedIn(isLoggedIn: Boolean) {
        prefs.edit().putBoolean("isLoggedIn", isLoggedIn).apply()
    }

    fun isLoggedIn(): Boolean {
        return prefs.getBoolean("isLoggedIn", false)
    }

    fun clear() {
        prefs.edit().clear().apply()
    }
}