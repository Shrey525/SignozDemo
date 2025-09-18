package com.example.test5000.data.api

import com.example.test5000.util.OpenTelemetryInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private val client = OkHttpClient.Builder()
        .addInterceptor(OpenTelemetryInterceptor()) //  attach your tracing interceptor
        .build()

    val api: CatApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.thecatapi.com/") //  base URL
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CatApiService::class.java)
    }
}
