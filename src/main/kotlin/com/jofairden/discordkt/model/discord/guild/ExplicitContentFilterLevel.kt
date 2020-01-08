package com.jofairden.discordkt.model.discord.guild

import com.fasterxml.jackson.annotation.JsonValue

enum class ExplicitContentFilterLevel(
    @JsonValue
    val level: Int
) {
    Disabled(0),
    MembersWithoutRoles(1),
    AllMembers(2)
}
