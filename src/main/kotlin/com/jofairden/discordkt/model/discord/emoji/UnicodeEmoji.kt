package com.jofairden.discordkt.model.discord.emoji

import com.fasterxml.jackson.annotation.JsonProperty

data class UnicodeEmoji(
    @JsonProperty("id")
    override val id: Long?,
    @JsonProperty("name")
    override val name: String?
) : IEmoji
