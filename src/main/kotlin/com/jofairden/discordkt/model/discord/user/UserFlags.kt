package com.jofairden.discordkt.model.discord.user

import com.fasterxml.jackson.annotation.JsonValue

enum class UserFlags(
    @JsonValue
    val flags: Int
) {
    None(0),
    DiscordEmployee(1 shl 0),
    DiscordPartner(1 shl 1),
    HypeSquadEvents(1 shl 2),
    BugHunter(1 shl 3),
    HouseBravery(1 shl 6),
    HouseBriliance(1 shl 7),
    HouseBalance(1 shl 8),
    EarlySupporter(1 shl 9),
    TeamUser(1 shl 10),
    System(1 shl 12),
}
