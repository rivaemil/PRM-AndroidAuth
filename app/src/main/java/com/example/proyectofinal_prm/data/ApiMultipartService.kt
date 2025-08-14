package com.example.proyectofinal_prm.data

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiMultipartService {
    @Multipart
    @POST("products")
    suspend fun uploadProduct(
        @Part("name") name: RequestBody,
        @Part("description") description: RequestBody,
        @Part("price") price: RequestBody,
        @Part images: List<MultipartBody.Part>
    ): Response<Unit>

    @Multipart
    @POST("products/{id}?_method=PUT")
    suspend fun updateProduct(
        @Path("id") id: Int,
        @Part("name") name: RequestBody,
        @Part("description") description: RequestBody,
        @Part("price") price: RequestBody,
        @Part images: List<MultipartBody.Part> // incluye nuevas imágenes y/o deletedImageUrls[] si lo manejas así
    ): Response<Unit>


}
