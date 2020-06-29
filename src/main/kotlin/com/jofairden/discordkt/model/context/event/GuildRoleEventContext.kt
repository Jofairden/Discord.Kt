package com.jofairden.discordkt.model.context.event

import com.fasterxml.jackson.annotation.JsonProperty
import com.jofairden.discordkt.api.DiscordClient
import com.jofairden.discordkt.model.discord.role.GuildRole

data class GuildRoleEventContext(
    @JsonProperty("guild_id")
    val guildId: Long,

    @JsonProperty("role")
    val role: GuildRole
) : IEventContext {
    override lateinit var discordClient: DiscordClient
}
