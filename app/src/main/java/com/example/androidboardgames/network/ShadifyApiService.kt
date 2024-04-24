package com.example.androidboardgames.network

import com.example.androidboardgames.data.ShadifyMath
import com.example.androidboardgames.data.ShadifyMemory
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://shadify.dev/api/"
private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()
interface ShadifyApiService{
    @GET("math/mul")
    suspend fun getMultiplicationEquation():ShadifyMath
    @GET("memory/generator")
    suspend fun getMemoryGame():ShadifyMemory
}

object ShadifyMathApi {
    val retrofitService: ShadifyApiService by lazy{
        retrofit.create(ShadifyApiService::class.java)
    }
}