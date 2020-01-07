package com.jofairden.kotlinkt.util

import com.jofairden.kotlinkt.retrofit.GatewayService
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

object RetrofitUtil {
    private val discordApi = Retrofit.Builder()
        .baseUrl("https://discordapp.com/api/")
        .addConverterFactory(JacksonConverterFactory.create())
        .build()

    val gatewayService = discordApi.create(GatewayService::class.java)
}
