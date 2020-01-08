package com.jofairden.discordkt.model.discord.guild

import com.fasterxml.jackson.annotation.JsonValue

enum class GuildVerificationLevel(
    @JsonValue
    val level: Int
) {
    None(0),
    Low(1),
    Medium(2),
    High(3),
    VeryHigh(4)
}
