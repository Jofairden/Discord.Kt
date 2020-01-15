package com.jofairden.discordkt.model.context.event

import com.fasterxml.jackson.annotation.JsonProperty
import com.jofairden.discordkt.api.DiscordClient
import com.jofairden.discordkt.model.discord.guild.GuildUser

data class TypingStartEventContext(
    @JsonProperty("channel_id")
    val channelId: Long,

    @JsonProperty("guild_id")
    val guildId: Long?,

    @JsonProperty("user_id")
    val userId: Long,

    @JsonProperty("timestamp")
    val unixTimestamp: Int,

    @JsonProperty("member")
    val user: GuildUser?
) : IEventContext {
    override lateinit var discordClient: DiscordClient
}

