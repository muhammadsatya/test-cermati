package com.example.test_cermati.ApiService

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.http.QueryMap
import retrofit2.http.Url
import java.util.concurrent.TimeUnit

class ApiRequest() {
    companion object{
        val API_GET_USER = "search/users"
    }

    val client = OkHttpClient().newBuilder()
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()
    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.github.com/")
        .client(client)
        .build()

    fun service(): ApiInterface{
        return retrofit.create(ApiInterface::class.java)
    }
}