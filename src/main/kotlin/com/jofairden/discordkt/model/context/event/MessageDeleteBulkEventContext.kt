package com.jofairden.discordkt.model.context.event

import com.fasterxml.jackson.annotation.JsonProperty

data class MessageDeleteBulkEventContext(
    @JsonProperty("ids")
    val messageIds: Array<Long>,

    @JsonProperty("channel_id")
    val channelId: Long,

    @JsonProperty("guild_id")
    val guildId: Long?
)
