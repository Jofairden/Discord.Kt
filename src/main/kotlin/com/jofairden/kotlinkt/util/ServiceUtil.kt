package com.jofairden.kotlinkt.util

import com.jofairden.kotlinkt.services.ChannelService
import com.jofairden.kotlinkt.services.GatewayService
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

internal object ServiceUtil {
    private val discordApi = Retrofit.Builder()
        .baseUrl("https://discordapp.com/api/")
        .addConverterFactory(JacksonConverterFactory.create())
        .build()

    val gatewayService = discordApi.create(GatewayService::class.java)
    val channelService = discordApi.create(ChannelService::class.java)
}
