package com.example.proyectofinal_prm.data

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

    @POST("products")
    suspend fun createProduct(@Body product: ProductCreateRequest): ProductItem

    @PUT("products/{id}")
    suspend fun updateProduct(
        @Path("id") id: Int,
        @Body product: ProductUpdateRequest
    ): ProductItem

    @DELETE("products/{id}")
    suspend fun deleteProduct(@Path("id") id: Int): Unit
}
