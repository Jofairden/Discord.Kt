package com.jofairden.discordkt.model.discord.channel

import com.fasterxml.jackson.annotation.JsonProperty

data class Overwrite(
    @JsonProperty("id")
    val id: Long,
    @JsonProperty("type")
    val type: OverwriteType,
    @JsonProperty("allow")
    val allow: Int,
    @JsonProperty("deny")
    val deny: Int
)
