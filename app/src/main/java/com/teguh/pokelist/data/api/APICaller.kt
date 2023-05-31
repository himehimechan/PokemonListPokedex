package com.teguh.pokelist.data.api

import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.GsonBuilder
import com.teguh.pokelist.BuildConfig
import com.teguh.pokelist.utils.MyContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import okhttp3.Interceptor


class APICaller {
    private var gson = GsonBuilder()
        .setDateFormat("yyyy-MM-dd HH:mm:ss")
        .create()

    companion object {
        val shared = APICaller()
    }

    private val retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .client(getClient())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addConverterFactory(GsonConverterFactory.create()).build()

    private fun getClient(): OkHttpClient {
        return OkHttpClient().newBuilder()
            .addInterceptor(ChuckerInterceptor(MyContext.context!!))
            .connectTimeout(60.toLong(), TimeUnit.SECONDS)
            .readTimeout(60.toLong(), TimeUnit.SECONDS)
            .writeTimeout(60.toLong(), TimeUnit.SECONDS)
            .build()
    }

    fun <T> buildService(service: Class<T>): T {
        return retrofit.create(service)
    }

}