package com.jofairden.discordkt.model.context.event

import com.fasterxml.jackson.annotation.JsonProperty
import com.jofairden.discordkt.model.discord.user.DiscordUser

data class GuildMemberUpdateEventContext(
    @JsonProperty("guild_id")
    val guildId: Long,

    @JsonProperty("roles")
    val roleIds: Array<Long>,

    @JsonProperty("user")
    val user: DiscordUser,

    @JsonProperty("nick")
    val nickname: String
)
