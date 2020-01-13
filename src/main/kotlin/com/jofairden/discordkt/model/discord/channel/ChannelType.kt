package com.jofairden.discordkt.model.discord.channel

import com.fasterxml.jackson.annotation.JsonValue

enum class ChannelType(
    @JsonValue
    val id: Int
) {
    GUILD_TEXT(0),
    DM(1),
    GUILD_VOICE(2),
    GROUP_DM(3),
    GUILD_CATEGORY(4),
    GUILD_NEWS(5),
    GUILD_STORE(6),
}
