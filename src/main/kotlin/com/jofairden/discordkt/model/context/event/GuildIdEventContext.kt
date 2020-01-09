package com.jofairden.discordkt.model.context.event

import com.fasterxml.jackson.annotation.JsonProperty

data class GuildIdEventContext(
    @JsonProperty("guild_id")
    val guildId: Long
)
