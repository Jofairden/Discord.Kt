package com.jofairden.discordkt.services

import com.jofairden.discordkt.model.discord.channel.DiscordChannel
import com.jofairden.discordkt.model.discord.guild.PartialGuild
import com.jofairden.discordkt.model.discord.user.Connection
import com.jofairden.discordkt.model.discord.user.DiscordUser
import com.jofairden.discordkt.model.request.ModifyBotUserBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Source: https://discordapp.com/developers/docs/resources/user
 */
interface UserService {
    @GET("users/@me")
    suspend fun getBotUser(): DiscordUser

    @GET("users/{user-id}")
    suspend fun getUser(
        @Path("user-id") userId: Long
    ): DiscordUser

    @PATCH("users/@me")
    suspend fun modifyBotUser(
        @Body body: ModifyBotUserBody
    ): DiscordUser

    @PATCH("users/@me")
    suspend fun modifyBotUser(
        @Field("username") username: String
    ): DiscordUser

    @GET("users/@me/guilds")
    suspend fun getBotUserGuilds(): ArrayList<PartialGuild> // PartialGuild

    @DELETE("users/@me/guilds/{guild-id}")
    suspend fun leaveGuild(
        @Path("guild-id") guildId: Long
    ): Response<Unit>

    @POST("users/@me/channels")
    suspend fun createDMChannel(
        @Field("recipient_id") recipientId: Long
    ): DiscordChannel // DMChannel

    @GET("users/@me/connections")
    suspend fun getBotUserConnections(): Array<Connection> // Connection
}
