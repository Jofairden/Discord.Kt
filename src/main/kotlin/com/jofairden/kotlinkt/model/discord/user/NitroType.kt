package com.jofairden.kotlinkt.model.discord.user

import com.fasterxml.jackson.annotation.JsonValue

enum class NitroType(
    @JsonValue
    val type: Int
) {
    NitroClassic(1),
    Nitro(2)
}
