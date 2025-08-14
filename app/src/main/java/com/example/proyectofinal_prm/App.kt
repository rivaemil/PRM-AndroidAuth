package com.example.proyectofinal_prm

import android.app.Application
import com.example.proyectofinal_prm.data.Session
import com.example.proyectofinal_prm.data.TokenStore

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        // Cargar token persistido para que el interceptor lo tenga desde el arranque
        val stored = TokenStore(this).load()
        if (!stored.isNullOrBlank()) {
            Session.setToken(stored)
        }
    }
}
