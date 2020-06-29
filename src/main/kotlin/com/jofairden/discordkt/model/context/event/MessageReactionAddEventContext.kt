package com.jofairden.discordkt.model.context.event

import com.fasterxml.jackson.annotation.JsonProperty
import com.jofairden.discordkt.api.DiscordClient
import com.jofairden.discordkt.api.cache.CombinedId
import com.jofairden.discordkt.api.cache.getSuspending
import com.jofairden.discordkt.model.discord.emoji.DiscordEmoji
import com.jofairden.discordkt.model.discord.guild.GuildUser
import com.jofairden.discordkt.util.lazyAsync

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

    @JsonProperty("emoji")
    val emoji: DiscordEmoji
) : IEventContext {
    override lateinit var discordClient: DiscordClient

    val channel by lazyAsync { discordClient.dataCache.channelCache.getSuspending(channelId) }
    val message by lazyAsync { discordClient.dataCache.messageCache.getSuspending(CombinedId(channelId, messageId)) }
}
