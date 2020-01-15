package com.jofairden.discordkt.model.discord.channel

import com.fasterxml.jackson.annotation.JsonProperty
import com.jofairden.discordkt.model.api.ServiceProviderAware
import com.jofairden.discordkt.model.discord.user.DiscordUser
import com.jofairden.discordkt.model.request.CreateMessageBody
import java.util.Date

data class DiscordChannel(
    @JsonProperty("id")
    val id: Long,
    @JsonProperty("type")
    val channelType: ChannelType,
    @JsonProperty("guild_id")
    val guildId: Long?,
    @JsonProperty("position")
    val position: Int?,
    @JsonProperty("permission_overwrites")
    val permissionOverwrites: Array<Overwrite>?,
    @JsonProperty("name")
    val name: String?,
    @JsonProperty("topic")
    val topic: String?,
    @JsonProperty("nsfw")
    val nsfw: Boolean?,
    @JsonProperty("last_message_id")
    val lastMessageId: Long?,
    @JsonProperty("bitrate")
    val bitrate: Int?,
    @JsonProperty("user_limit")
    val userLimit: Int?,
    @JsonProperty("rate_limit_per_user")
    val rateLimitPerUser: Int?,
    @JsonProperty("recipients")
    val recipients: Array<DiscordUser>?,
    @JsonProperty("icon")
    val iconHash: String?,
    @JsonProperty("owner_id")
    val ownerId: Long?,
    @JsonProperty("application_id")
    val applicationId: Long?,
    @JsonProperty("parent_id")
    val parentId: Long?,
    @JsonProperty("last_pin_timestamp")
    val lastPinTimestamp: Date?
) : ServiceProviderAware() {

    suspend fun sendMessage(text: String) {
        serviceProvider.channelService.postChannelMessage(
            id,
            CreateMessageBody(content = text)
        )
    }
}
