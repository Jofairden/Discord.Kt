package com.jofairden.discordkt.model.discord.guild

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class PartialGuild(
    @JsonProperty("id")
    val id: Long,

    @JsonProperty("name")
    val name: String,

    @JsonProperty("icon")
    val iconHash: String,

    @JsonProperty("owner")
    val isOwner: Boolean,

    @JsonProperty("permissions")
    val permissions: Int,

    @JsonProperty("splash")
    val splashHash: String?

)
