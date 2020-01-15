package com.jofairden.discordkt.model.context.event

import com.fasterxml.jackson.annotation.JsonProperty
import com.jofairden.discordkt.api.DiscordClient
import com.jofairden.discordkt.model.discord.user.DiscordUser

data class GuildBanEventContext(
    @JsonProperty("guild_id")
    val guildId: Long,

    @JsonProperty("user")
    val user: DiscordUser
) : IEventContext {
    override lateinit var discordClient: DiscordClient
}
