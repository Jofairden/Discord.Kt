package com.jofairden.kotlinkt.services

import com.fasterxml.jackson.databind.JsonNode
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Source: https://discordapp.com/developers/docs/resources/emoji
 */
interface EmojiService {
    @GET("guilds/{guild.id}/emojis")
    suspend fun getGuildEmojis(
        @Path("guild.id") guildId: Int
    ): ArrayList<JsonNode> // emoji

    @GET("guilds/{guild.id}/emojis/{emoji.id}")
    suspend fun getGuildEmoji(
        @Path("guild.id") guildId: Int,
        @Path("emoji.id") emojiId: Int
    ): JsonNode // emoji

    @POST("guilds/{guild.id}/emojis")
    suspend fun createGuildEmoji(
        @Path("guild.id") guildId: Int
    ): JsonNode // emoji

    //MANAGE_EMOJIS
    @PATCH("guilds/{guild.id}/emojis/{emoji.id}")
    suspend fun modifyGuildEmoji(
        @Path("guild.id") guildId: Int,
        @Path("emoji.id") emojiId: Int
    ): JsonNode // emoji

    //MANAGE_EMOJIS
    @PATCH("guilds/{guild.id}/emojis/{emoji.id}")
    suspend fun deleteGuildEmoji(
        @Path("guild.id") guildId: Int,
        @Path("emoji.id") emojiId: Int
    ): JsonNode // 204 No content
}
