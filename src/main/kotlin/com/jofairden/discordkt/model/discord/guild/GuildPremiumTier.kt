package com.jofairden.discordkt.model.discord.guild

import com.fasterxml.jackson.annotation.JsonValue

enum class GuildPremiumTier(
    @JsonValue
    val value: Int
) {
    None(0),
    Tier1(1),
    Tier2(2),
    Tier3(3)
}
