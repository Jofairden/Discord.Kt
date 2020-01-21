package com.jofairden.discordkt.model.discord.emoji

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.JsonNode

data class DiscordEmoji(
    @JsonProperty("id")
    override val id: Long?,
    @JsonProperty("name")
    override val name: String?,
    @JsonProperty("roles")
    val roles: Array<JsonNode>? = null,
    @JsonProperty("user")
    val creator: JsonNode? = null, // DiscordUser
    @JsonProperty("require_colons")
    val requireColons: Boolean? = null,
    @JsonProperty("managed")
    val managed: Boolean? = null,
    @JsonProperty("amimated")
    val animated: Boolean? = null
) : IEmoji

