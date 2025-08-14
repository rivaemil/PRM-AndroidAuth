// data/ApiClient.kt
package com.example.proyectofinal_prm.data

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val req = chain.request()
        val token = Session.bearerToken
        val newReq = if (!token.isNullOrBlank()) {
            req.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else req
        return chain.proceed(newReq)
    }
}

object ApiClient {
    private val okHttp = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor())
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:8000/api/") // tu base actual
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttp)
        .build()

    // Mantén tu servicio existente
    val apiService: ApiService = retrofit.create(ApiService::class.java)

    // Servicio para multipart (crear/editar con imágenes)
    val multipartService: ApiMultipartService = retrofit.create(ApiMultipartService::class.java)

    // Servicio de auth (login)
    val authApi: AuthApi = retrofit.create(AuthApi::class.java)
}
