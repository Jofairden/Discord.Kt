package com.jofairden.discordkt.model.context.event

import com.fasterxml.jackson.annotation.JsonProperty
import com.jofairden.discordkt.api.DiscordClient
import com.jofairden.discordkt.api.cache.getSuspending
import com.jofairden.discordkt.model.discord.guild.GuildUser
import com.jofairden.discordkt.util.lazyAsync
import kotlin.properties.Delegates

// https://discordapp.com/developers/docs/topics/gateway#guild-member-add
data class GuildMemberAddEventContext(
    @JsonProperty("user")
    val user: GuildUser
) : IEventContext {

    override lateinit var discordClient: DiscordClient

    var guildId by Delegates.notNull<Long>()
        internal set

    val guild by lazyAsync { discordClient.dataCache.guildCache.getSuspending(guildId) }
}
