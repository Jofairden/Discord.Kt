package com.jofairden.discordkt.model.discord

import com.fasterxml.jackson.annotation.JsonValue

data class PermissionBitSet(
    @JsonValue
    val value: Int
)
