package com.jofairden.discordkt.model.discord.guild

import com.fasterxml.jackson.annotation.JsonProperty

data class GuildEmbed(
    @JsonProperty("enabled")
    val enabled: Boolean,
    @JsonProperty("channel_id")
    val channelId: Long?
)
