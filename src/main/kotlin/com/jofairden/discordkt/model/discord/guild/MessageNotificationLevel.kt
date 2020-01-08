package com.jofairden.discordkt.model.discord.guild

import com.fasterxml.jackson.annotation.JsonValue

enum class MessageNotificationLevel(
    @JsonValue
    val level: Int
) {
    AllMessages(0),
    OnlyMentions(1)
}
