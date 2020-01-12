package com.jofairden.discordkt.model.discord

import com.fasterxml.jackson.annotation.JsonCreator

data class PermissionBitSet(
    val value: Int
) {
    companion object {
        @JvmStatic
        @JsonCreator
        fun create(value: Int): PermissionBitSet = PermissionBitSet(value)
    }
}
