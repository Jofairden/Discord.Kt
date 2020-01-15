package com.jofairden.discordkt.model.context.event

import com.fasterxml.jackson.annotation.JsonProperty
import com.jofairden.discordkt.api.DiscordClient

data class GuildIdEventContext(
    @JsonProperty("guild_id")
    val guildId: Long
) : IEventContext {
    override lateinit var discordClient: DiscordClient
}

