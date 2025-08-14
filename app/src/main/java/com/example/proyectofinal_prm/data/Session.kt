package com.example.proyectofinal_prm.data

object Session {
    var bearerToken: String? = null
        private set

    fun setToken(token: String?) {
        bearerToken = token
    }

    fun clear() {
        bearerToken = null
    }
}