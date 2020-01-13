package com.jofairden.discordkt.model.context.event

import com.fasterxml.jackson.annotation.JsonProperty
import com.jofairden.discordkt.api.cache.DataCache
import com.jofairden.discordkt.api.cache.getSuspending
import com.jofairden.discordkt.model.discord.guild.GuildUser
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlin.properties.Delegates

// https://discordapp.com/developers/docs/topics/gateway#guild-member-add
data class GuildMemberAddEventContext(
    @JsonProperty("user")
    val user: GuildUser,
    private val dataCache: DataCache
) {
    var guildId by Delegates.notNull<Long>()
        internal set

    val guild = GlobalScope.async(Dispatchers.Unconfined, start = CoroutineStart.LAZY) {
        dataCache.guildCache.getSuspending(guildId)
    }
}

