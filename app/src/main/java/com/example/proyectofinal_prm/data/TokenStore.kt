// data/TokenStore.kt
package com.example.proyectofinal_prm.data

import android.content.Context
import androidx.core.content.edit

class TokenStore(private val context: Context) {
    private val prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    fun save(token: String) {
        prefs.edit { putString("bearer_token", token) }
    }

    fun load(): String? = prefs.getString("bearer_token", null)

    fun clear() { prefs.edit { remove("bearer_token") } }
}
