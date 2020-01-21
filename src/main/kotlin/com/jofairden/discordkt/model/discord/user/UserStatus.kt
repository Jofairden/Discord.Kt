package com.jofairden.discordkt.model.discord.user

import com.fasterxml.jackson.annotation.JsonValue

enum class UserStatus(
    @JsonValue
    val code: String
) {
    Online("online"),
    DoNotDisturb("dnd"),
    Idle("idle"),
    Invisible("invisible"),
    Offline("offline")
}
