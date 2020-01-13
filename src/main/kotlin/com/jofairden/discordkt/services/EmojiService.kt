package com.jofairden.discordkt.services

import com.fasterxml.jackson.databind.JsonNode
import com.jofairden.discordkt.model.discord.emoji.DiscordEmoji
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Source: https://discordapp.com/developers/docs/resources/emoji
 */
interface EmojiService {
    @GET("guilds/{guild-id}/emojis")
    suspend fun getGuildEmojis(
        @Path("guild-id") guildId: Long
    ): Array<DiscordEmoji> // emoji

    @GET("guilds/{guild-id}/emojis/{emoji-id}")
    suspend fun getGuildEmoji(
        @Path("guild-id") guildId: Long,
        @Path("emoji-id") emojiId: Long
    ): DiscordEmoji // emoji

    @POST("guilds/{guild-id}/emojis")
    suspend fun createGuildEmoji(
        @Path("guild-id") guildId: Long
    ): DiscordEmoji // emoji

    //MANAGE_EMOJIS
    @PATCH("guilds/{guild-id}/emojis/{emoji-id}")
    suspend fun modifyGuildEmoji(
        @Path("guild-id") guildId: Long,
        @Path("emoji-id") emojiId: Long
    ): DiscordEmoji // emoji

    //MANAGE_EMOJIS
    @PATCH("guilds/{guild-id}/emojis/{emoji-id}")
    suspend fun deleteGuildEmoji(
        @Path("guild-id") guildId: Long,
        @Path("emoji-id") emojiId: Long
    ): JsonNode // 204 No content
}
