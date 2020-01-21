package com.jofairden.discordkt.model.discord.emoji

import com.fasterxml.jackson.annotation.JsonProperty

data class UnicodeEmoji(
    @JsonProperty("name")
    override val name: String?,
    @JsonProperty("id")
    override val id: Long? = null
) : IEmoji
