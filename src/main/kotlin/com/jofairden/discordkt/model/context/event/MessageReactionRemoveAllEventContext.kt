package com.jofairden.discordkt.model.context.event

import com.fasterxml.jackson.annotation.JsonProperty
import com.jofairden.discordkt.api.DiscordClient

data class MessageReactionRemoveAllEventContext(
    @JsonProperty("channel_id")
    val channelId: Long,

    @JsonProperty("message_id")
    val messageId: Long,

    @JsonProperty("guild_id")
    val guildId: Long?
) : IEventContext {
    override lateinit var discordClient: DiscordClient
}

