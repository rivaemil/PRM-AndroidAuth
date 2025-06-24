package com.example.proyectofinal_prm.data

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.GET

interface ApiService {
    @POST("register")
    fun register(@Body user: RegisterRequest): Call<AuthResponse>

    @POST("login")
    fun login(@Body user: LoginRequest): Call<AuthResponse>

    @GET("images")
    suspend fun getImages(): List<ImageItem>
}

