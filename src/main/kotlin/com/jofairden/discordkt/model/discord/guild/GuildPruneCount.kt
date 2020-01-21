package com.jofairden.discordkt.model.discord.guild

import com.fasterxml.jackson.annotation.JsonProperty

data class GuildPruneCount(
    @JsonProperty("pruned")
    val count: Int?
)
