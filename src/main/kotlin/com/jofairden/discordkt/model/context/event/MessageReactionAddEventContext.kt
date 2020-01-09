package com.jofairden.discordkt.model.context.event

import com.fasterxml.jackson.annotation.JsonProperty
import com.jofairden.discordkt.model.discord.emoji.DiscordEmoji
import com.jofairden.discordkt.model.discord.guild.GuildUser

data class MessageReactionAddEventContext(
    @JsonProperty("user_id")
    val userId: Long,

    @JsonProperty("channel_id")
    val channelId: Long,

    @JsonProperty("message_id")
    val messageId: Long,

    @JsonProperty("guild_id")
    val guildId: Long?,

    @JsonProperty("member")
    val user: GuildUser?,

    // TODO partial?  https://discordapp.com/developers/docs/topics/gateway#message-reaction-add
    @JsonProperty("emoji")
    val emoji: DiscordEmoji
)

