package com.jofairden.discordkt.model.context.event

import com.fasterxml.jackson.annotation.JsonProperty

data class MessageReactionRemoveAllEventContext(
    @JsonProperty("channel_id")
    val channelId: Long,

    @JsonProperty("message_id")
    val messageId: Long,

    @JsonProperty("guild_id")
    val guildId: Long?
)
