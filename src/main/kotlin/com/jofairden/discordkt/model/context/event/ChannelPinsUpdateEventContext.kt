package com.jofairden.discordkt.model.context.event

import com.fasterxml.jackson.annotation.JsonProperty
import com.jofairden.discordkt.api.DiscordClient

data class ChannelPinsUpdateEventContext(
    @JsonProperty("guild_id")
    val guildId: Long?,

    @JsonProperty("channel_id")
    val channelId: Long,

    @JsonProperty("last_pin_timestamp")
    val timestamp: String?
) : IEventContext {
    override lateinit var discordClient: DiscordClient
}
