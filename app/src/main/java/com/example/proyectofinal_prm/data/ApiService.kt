package com.example.proyectofinal_prm.data

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.Response
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.GET
import retrofit2.http.*

interface ApiService {
    @POST("register")
    fun register(@Body user: RegisterRequest): Call<AuthResponse>

    @POST("login")
    fun login(@Body user: LoginRequest): Call<AuthResponse>

    @GET("articles")
    suspend fun getProducts(): ProductResponse

    @GET("products/{id}")
    suspend fun getProduct(@Path("id") id: Int): ProductItem

    @PUT("products/{id}")
    suspend fun updateProduct(@Path("id") id: Int, @Body product: ProductRequest): ProductItem

    @DELETE("products/{id}")
    suspend fun deleteProduct(@Path("id") id: Int): retrofit2.Response<Unit>

}
