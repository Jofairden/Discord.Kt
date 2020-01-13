package com.jofairden.discordkt.model.discord.channel

import com.fasterxml.jackson.annotation.JsonValue

enum class OverwriteType(
    @JsonValue
    val type: String
) {
    Role("role"),
    Member("member")
}
