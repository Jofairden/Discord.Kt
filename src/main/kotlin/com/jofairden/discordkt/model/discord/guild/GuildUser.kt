package com.jofairden.discordkt.model.discord.guild

import com.fasterxml.jackson.annotation.JsonProperty
import com.jofairden.discordkt.model.discord.user.DiscordUser

data class GuildUser(
    @JsonProperty("user")
    val discordUser: DiscordUser,

    @JsonProperty("nick")
    val nickname: String?,

    @JsonProperty("roles")
    val roleIds: Array<Long>,

    @JsonProperty("joined_at")
    val joinedAt: String,

    @JsonProperty("premium_since")
    val boostedSince: String?,

    @JsonProperty("deaf")
    val isDeafenedInVoice: Boolean,

    @JsonProperty("muted")
    val isMutedInVoice: Boolean
)
