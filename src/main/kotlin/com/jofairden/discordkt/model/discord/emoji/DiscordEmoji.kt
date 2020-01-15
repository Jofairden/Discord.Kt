package com.jofairden.discordkt.model.discord.emoji

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.JsonNode

data class DiscordEmoji(
    @JsonProperty("id")
    override val id: Long?,
    @JsonProperty("name")
    override val name: String?,
    @JsonProperty("roles")
    val roles: Array<JsonNode>?,
    @JsonProperty("user")
    val creator: JsonNode?, // DiscordUser
    @JsonProperty("require_colons")
    val requireColons: Boolean?,
    @JsonProperty("managed")
    val managed: Boolean?,
    @JsonProperty("amimated")
    val animated: Boolean?
) : IEmoji

