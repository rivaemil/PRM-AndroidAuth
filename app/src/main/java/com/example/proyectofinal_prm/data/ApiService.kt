package com.example.proyectofinal_prm.data

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @POST("register")
    fun register(@Body user: RegisterRequest): retrofit2.Call<AuthResponse>

    @POST("login")
    fun login(@Body user: LoginRequest): retrofit2.Call<AuthResponse>

    @GET("products")
    suspend fun getProducts(): ProductResponse

    @GET("products/{id}")
    suspend fun getProduct(@Path("id") id: Int): ProductItem

    @PUT("products/{id}")
    suspend fun updateProduct(@Path("id") id: Int, @Body product: ProductRequest): ProductItem

    @DELETE("products/{id}")
    suspend fun deleteProduct(@Path("id") id: Int): Response<Unit>

    @Multipart
    @POST("products/{id}?_method=PUT")
    suspend fun updateProductMultipart(
        @Path("id") id: Int,
        @Part("name") name: RequestBody,
        @Part("description") description: RequestBody,
        @Part("price") price: RequestBody,
        @Part parts: List<MultipartBody.Part> // images[] + deletedImageUrls[]
    ): Response<Unit>
}

interface AuthApi {
    @POST("login")
    suspend fun login(@Body req: LoginRequest): Response<AuthResponse>
}
