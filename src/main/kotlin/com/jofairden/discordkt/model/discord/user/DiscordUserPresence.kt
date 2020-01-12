package com.jofairden.discordkt.model.discord.user

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.JsonNode

data class DiscordUserPresence(
    @JsonProperty("user")
    val user: JsonNode,

    @JsonProperty("roles")
    val roleIds: Array<Long>?,

    @JsonProperty("game")
    val activity: JsonNode?,

    @JsonProperty("guild_id")
    val guildId: Long,

    @JsonProperty("status")
    val status: String,

    @JsonProperty("activities")
    val activities: Array<JsonNode>,

    @JsonProperty("client_status")
    val clientStatus: JsonNode,

    @JsonProperty("premium_since")
    val nitroBoostingSince: String?,

    @JsonProperty("nick")
    val nickname: String?
)
