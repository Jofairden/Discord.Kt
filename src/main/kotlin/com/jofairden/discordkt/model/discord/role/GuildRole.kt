package com.jofairden.discordkt.model.discord.role

import com.fasterxml.jackson.annotation.JsonProperty
import com.jofairden.discordkt.model.discord.PermissionBitSet

data class GuildRole(

    @JsonProperty(" id")
    val id: Long,

    @JsonProperty("name")
    val name: String,

    @JsonProperty("color")
    val color: Int,

    @JsonProperty("hoist")
    val isHoisted: Boolean,

    @JsonProperty("position")
    val position: Int,

    @JsonProperty("permissions")
    val permissions: PermissionBitSet,

    @JsonProperty("managed")
    val isManagedByIntegration: Boolean,

    @JsonProperty("mentionable")
    val isMentionable: Boolean
)

