package com.jofairden.discordkt.model.context.event

import com.fasterxml.jackson.annotation.JsonProperty
import com.jofairden.discordkt.api.DiscordClient
import com.jofairden.discordkt.model.discord.emoji.DiscordEmoji

data class GuildEmojisUpdateEventContext(
    @JsonProperty("guild_id")
    val guildId: Long,

    @JsonProperty("emojis")
    val emojis: Array<DiscordEmoji>
) : IEventContext {
    override lateinit var discordClient: DiscordClient
}
