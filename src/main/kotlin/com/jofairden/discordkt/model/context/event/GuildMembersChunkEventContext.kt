package com.jofairden.discordkt.model.context.event

import com.fasterxml.jackson.annotation.JsonProperty
import com.jofairden.discordkt.api.DiscordClient
import com.jofairden.discordkt.model.discord.guild.GuildUser
import com.jofairden.discordkt.model.discord.user.DiscordUserPresence

data class GuildMembersChunkEventContext(

    @JsonProperty("guild_id")
    val guildId: Long,

    @JsonProperty("members")
    val members: Array<GuildUser>,

    @JsonProperty("not_found")
    val notFound: Any, // ??

    @JsonProperty("presences")
    val presences: Array<DiscordUserPresence>
) : IEventContext {
    override lateinit var discordClient: DiscordClient
}

