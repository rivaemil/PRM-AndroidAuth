package com.example.proyectofinal_prm.api

import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @FormUrlEncoded
    @POST("/login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<UserResponse>

    @FormUrlEncoded
    @POST("/register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<UserResponse>

    @GET("/user")
    suspend fun getUser(): Response<UserResponse>

    @POST("/logout")
    suspend fun logout(): Response<Void>
}

data class UserResponse(
    val id: Int,
    val name: String,
    val email: String,
    val token: String? = null
)