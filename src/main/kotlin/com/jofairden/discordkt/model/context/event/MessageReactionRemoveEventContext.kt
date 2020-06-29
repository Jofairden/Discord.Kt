package com.jofairden.discordkt.model.context.event

import com.fasterxml.jackson.annotation.JsonProperty
import com.jofairden.discordkt.api.DiscordClient
import com.jofairden.discordkt.model.discord.emoji.DiscordEmoji

data class MessageReactionRemoveEventContext(
    @JsonProperty("user_id")
    val userId: Long,

    @JsonProperty("channel_id")
    val channelId: Long,

    @JsonProperty("message_id")
    val messageId: Long,

    @JsonProperty("guild_id")
    val guildId: Long?,

    @JsonProperty("emoji")
    val emoji: DiscordEmoji
) : IEventContext {
    override lateinit var discordClient: DiscordClient
}
