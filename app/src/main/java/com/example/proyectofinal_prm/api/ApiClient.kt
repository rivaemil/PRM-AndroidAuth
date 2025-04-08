package com.example.proyectofinal_prm.api

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// API Client
object ApiClient {
    private const val BASE_URL = "http://10.0.2.2:8000"

    private val cookieJar = object : CookieJar {
        private var cookies: List<Cookie> = listOf()

        override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
            this.cookies = cookies
        }

        override fun loadForRequest(url: HttpUrl): List<Cookie> = cookies
    }

    private val client = OkHttpClient.Builder()
        .cookieJar(cookieJar)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }).build()

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service: ApiService = retrofit.create(ApiService::class.java)
}
