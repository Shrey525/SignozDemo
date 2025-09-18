package com.example.test5000.data.api

import com.example.test5000.data.model.CatImage
import retrofit2.http.GET

interface CatApiService {
    @GET("v1/images/search")
    suspend fun getRandomCat(): List<CatImage>
}
