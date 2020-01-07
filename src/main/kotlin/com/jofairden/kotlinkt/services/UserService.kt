package com.jofairden.kotlinkt.services

import com.fasterxml.jackson.databind.JsonNode
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Source: https://discordapp.com/developers/docs/resources/user
 */
interface UserService {
    @GET("users/@me")
    suspend fun getBotUser()

    @GET("users/{user.id}")
    suspend fun getUser(
        @Path("user.id") userId: Int
    ): JsonNode // User

    @PATCH("users/@me")
    suspend fun modifyBotUser(): JsonNode // User

    @GET("users/@me/guilds")
    suspend fun getBotUserGuilds(): ArrayList<JsonNode> // PartialGuild

    @DELETE("users/@me/guilds/{guild.id}")
    suspend fun leaveGuild(
        @Path("guild.id") guildId: Int
    ): JsonNode

    @POST("users/@me/channels")
    suspend fun createDMChannel(): JsonNode // DMChannel

    @GET("users/@me/connections")
    suspend fun getBotUserConnections(): JsonNode // Connection
}
