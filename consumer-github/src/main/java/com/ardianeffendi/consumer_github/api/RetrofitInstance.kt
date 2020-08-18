package com.ardianeffendi.consumer_github.api

import com.ardianeffendi.consumer_github.utils.Constants.Companion.BASE_URL
import com.ardianeffendi.consumer_github.utils.Constants.Companion.BASE_URL_DETAIL
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitInstance {

    companion object {
        private val retrofit by lazy {
            val client = OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build()
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }

        private val retrofitDetail by lazy {
            val client = OkHttpClient.Builder()
                .build()
            Retrofit.Builder()
                .baseUrl(BASE_URL_DETAIL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }

        val api by lazy {
            retrofit.create(UsersApi::class.java)
        }

        val apiDetail by lazy {
            retrofitDetail.create(UsersApi::class.java)
        }
    }
}