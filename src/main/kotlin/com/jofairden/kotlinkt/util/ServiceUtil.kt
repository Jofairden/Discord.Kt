package com.jofairden.kotlinkt.util

import com.jofairden.kotlinkt.services.AuditLogService
import com.jofairden.kotlinkt.services.ChannelService
import com.jofairden.kotlinkt.services.EmojiService
import com.jofairden.kotlinkt.services.GatewayService
import com.jofairden.kotlinkt.services.GuildService
import com.jofairden.kotlinkt.services.InviteService
import com.jofairden.kotlinkt.services.UserService
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

internal object ServiceUtil {

    private const val API_URL = "https://discordapp.com/api/"

    private val discordApi = Retrofit.Builder()
        .baseUrl(API_URL)
        .addConverterFactory(JacksonConverterFactory.create())
        .build()

    val gatewayService = discordApi.create(GatewayService::class.java)
    val channelService = discordApi.create(ChannelService::class.java)
    val userService = discordApi.create(UserService::class.java)
    val auditLogService = discordApi.create(AuditLogService::class.java)
    val inviteService = discordApi.create(InviteService::class.java)
    val emojiService = discordApi.create(EmojiService::class.java)
    val guildService = discordApi.create(GuildService::class.java)
}
