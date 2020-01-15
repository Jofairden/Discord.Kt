package com.jofairden.discordkt.model.context.event

import com.fasterxml.jackson.annotation.JsonProperty
import com.jofairden.discordkt.api.DiscordClient

data class GuildRoleIdEventContext(
    @JsonProperty("guild_id")
    val guildId: Long,

    @JsonProperty("role_id")
    val roleId: Long
) : IEventContext {
    override lateinit var discordClient: DiscordClient
}

