package com.jofairden.discordkt.model.context.event

import com.fasterxml.jackson.annotation.JsonProperty
import com.jofairden.discordkt.model.discord.guild.GuildUser

// https://discordapp.com/developers/docs/topics/gateway#guild-member-add
data class GuildMemberAddEventContext(
    @JsonProperty("user")
    val user: GuildUser
)

