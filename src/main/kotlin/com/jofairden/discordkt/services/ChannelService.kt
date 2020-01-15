package com.jofairden.discordkt.services

import com.fasterxml.jackson.databind.JsonNode
import com.jofairden.discordkt.model.discord.channel.DiscordChannel
import com.jofairden.discordkt.model.discord.message.DiscordMessage
import com.jofairden.discordkt.model.request.CreateMessageBody
import com.jofairden.discordkt.model.request.EditMessageBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

/**
 * Source: https://discordapp.com/developers/docs/resources/channel
 */
interface ChannelService {
    @GET("channels/{channel-id}")
    suspend fun getChannel(
        @Path("channel-id") id: Long
    ): DiscordChannel

    // Requires MANAGE_CHANNELS perm
    @PUT("channels/{channel-id}")
    suspend fun modifyChannel(
        @Path("channel-id") id: Long
    ): DiscordChannel

    // Requires MANAGE_CHANNELS perm
    @DELETE("channels/{channel-id}")
    suspend fun deleteChannel(
        @Path("channel-id") id: Long
    ): DiscordChannel

    // Requires VIEW_CHANNEL perm
    @GET("channels/{channel-id}/messages")
    suspend fun getChannelMessages(
        @Path("channel-id") id: Long
    ): Array<DiscordMessage> // Message

    // Requires READ_MESSAGE_HISTORY perm
    @GET("channels/{channel-id}/messages/{message-id}")
    suspend fun getChannelMessage(
        @Path("channel-id") id: Long,
        @Path("message-id") messageId: Long
    ): DiscordMessage

    // Requires SEND_MESSAGES perm (SEND_TTS_MESSAGES with tts=true)
    @Headers("Content-Type: application/json")
    @POST("channels/{channel-id}/messages")
    suspend fun postChannelMessage(
        @Path("channel-id") id: Long,
        @Body body: CreateMessageBody
    ): DiscordMessage

    @PUT("channels/{channel-id}/messages/{message-id}/reactions/{emoji}/@me")
    suspend fun createReaction(
        @Path("channel-id") channelId: Long,
        @Path("message-id") messageId: Long,
        @Path("emoji") emoji: String
    ): JsonNode //204 empty

    @DELETE("channels/{channel-id}/messages/{message-id}/reactions/{emoji}/@me")
    suspend fun deleteReaction(
        @Path("channel-id") channelId: Long,
        @Path("message-id") messageId: Long,
        @Path("emoji") emoji: String
    ): JsonNode //204 empty

    @DELETE("channels/{channel-id}/messages/{message-id}/reactions/{emoji}/{user-id}")
    suspend fun deleteUserReaction(
        @Path("channel-id") channelId: Long,
        @Path("message-id") messageId: Long,
        @Path("emoji") emoji: String,
        @Path("user-id") userId: Long
    ): JsonNode //204 empty

    @GET("channels/{channel-id}/messages/{message-id}/reactions/{emoji}")
    suspend fun getReactions(
        @Path("channel-id") channelId: Long,
        @Path("message-id") messageId: Long,
        @Path("emoji") emoji: String
    ): Array<JsonNode> // User

    @DELETE("channels/{channel-id}/messages/{message-id}/reactions")
    suspend fun deleteAllReactions(
        @Path("channel-id") channelId: Long,
        @Path("message-id") messageId: Long
    ): JsonNode

    @PATCH("channels/{channel-id}/messages/{message-id}")
    suspend fun editMessage(
        @Path("channel-id") channelId: Long,
        @Path("message-id") messageId: Long,
        @Body body: EditMessageBody
    ): DiscordMessage

    @DELETE("channels/{channel-id}/messages/{message-id}")
    suspend fun deleteMessage(
        @Path("channel-id") channelId: Long,
        @Path("message-id") messageId: Long
    ): JsonNode // 204 empty

    @POST("channels/{channel-id}/messages/bulk-delete")
    suspend fun bulkDeleteMessages(
        @Path("channel-id") channelId: Long
    ): JsonNode //204 empty

    @PUT("channels/{channel-id}/permissions/{overwrite-id}")
    suspend fun editChannelPermissions(
        @Path("channel-id") channelId: Long,
        @Path("overwrite-id") overwriteId: Long
    ): JsonNode//204 empty

    @GET("channels/{channel-id}/invites")
    suspend fun getChannelInvites(
        @Path("channel-id") channelId: Long
    ): ArrayList<JsonNode>

    @POST("channels/{channel-id}/invites")
    suspend fun createChannelInvite(
        @Path("channel-id") channelId: Long
    ): JsonNode

    @DELETE("channels/{channel-id}/permissions/{overwrite-id}")
    suspend fun deleteChannelPermission(
        @Path("channel-id") channelId: Long,
        @Path("overwrite-id") overwriteId: Long
    ): JsonNode//204 empty

    @POST("channels/{channel-id}/typing")
    suspend fun triggerTypingIndicator(
        @Path("channel-id") channelId: Long
    ): JsonNode//204 empty

    @GET("channels/{channel-id}/pins")
    suspend fun getPinnedMessages(
        @Path("channel-id") channelId: Long
    ): Array<DiscordMessage>

    @PUT("channels/{channel-id}/pins/{message-id}")
    suspend fun addPinnedChannelMessage(
        @Path("channel-id") channelId: Long,
        @Path("message-id") messagedId: Long
    ): JsonNode//204 empty

    @DELETE("channels/{channel-id}/pins/{message-id}")
    suspend fun deletePinnedChannelMessage(
        @Path("channel-id") channelId: Long,
        @Path("message-id") messagedId: Long
    ): JsonNode//204 empty

    @PUT("channels/{channel-id}/recipients/{user-id}")
    suspend fun groupDMAddRecipient(
        @Path("channel-id") channelId: Long,
        @Path("user-id") userId: Long
    ): JsonNode

    @DELETE("channels/{channel-id}/recipients/{user-id}")
    suspend fun groupDMRemoveRecipient(
        @Path("channel-id") channelId: Long,
        @Path("user-id") userId: Long
    ): JsonNode
}
