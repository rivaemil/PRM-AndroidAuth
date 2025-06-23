package com.example.proyectofinal_prm.data

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("register")
    fun register(@Body user: RegisterRequest): Call<AuthResponse>

    @POST("login")
    fun login(@Body user: LoginRequest): Call<AuthResponse>
}
