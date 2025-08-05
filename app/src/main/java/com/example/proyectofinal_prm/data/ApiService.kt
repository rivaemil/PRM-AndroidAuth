package com.example.proyectofinal_prm.data

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {
    @POST("register")
    fun register(@Body user: RegisterRequest): Call<AuthResponse>

    @POST("login")
    fun login(@Body user: LoginRequest): Call<AuthResponse>

    @GET("products")
    suspend fun getProducts(): ProductResponse

    @Multipart
    @POST("products/{id}_method=PUT")
    suspend fun updateProduct(
        @Path("id") productId: Int,
        @Part("name") name: RequestBody,
        @Part("description") description: RequestBody,
        @Part("price") price: RequestBody,
        @Part images: List<MultipartBody.Part>,
        @Part deletedImageIds: RequestBody
    ): retrofit2.Response<ProductResponse>

    @POST("products/{id}/delete-images")
    suspend fun deleteProductImages(
        @Path("id") productId: Int,
        @Body imageUrls: List<String>
    ): Response<Unit>

    @GET("products/{id}")
    suspend fun getProductById(@Path("id") id: Int): Response<ProductItem>

    @Multipart
    @POST("products/{id}/upload-images")
    suspend fun uploadProductImages(
        @Path("id") id: Int,
        @Part images: List<MultipartBody.Part>
    ): Response<Unit>
}
