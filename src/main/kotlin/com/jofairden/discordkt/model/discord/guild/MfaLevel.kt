package com.jofairden.discordkt.model.discord.guild

import com.fasterxml.jackson.annotation.JsonValue

enum class MfaLevel(
    @JsonValue
    val level: Int
) {
    None(0),
    Elevated(1)
}
