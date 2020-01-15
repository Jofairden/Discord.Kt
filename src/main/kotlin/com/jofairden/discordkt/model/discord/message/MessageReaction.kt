package com.jofairden.discordkt.model.discord.message

import com.fasterxml.jackson.annotation.JsonProperty
import com.jofairden.discordkt.model.discord.emoji.IEmoji

data class MessageReaction(
    @JsonProperty("count")
    val count: Int,
    @JsonProperty("me")
    val me: Boolean,
    @JsonProperty("emoji")
    val emoji: IEmoji
)
