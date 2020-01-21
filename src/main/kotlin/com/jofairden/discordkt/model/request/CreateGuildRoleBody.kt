package com.jofairden.discordkt.model.request

import com.fasterxml.jackson.annotation.JsonProperty

data class CreateGuildRoleBody(
    @JsonProperty("name")
    val name: String? = null,
    @JsonProperty("permissions")
    val permissions: Int? = null,
    @JsonProperty("color")
    val color: Int? = null,
    @JsonProperty("hoist")
    val hoist: Boolean? = null,
    @JsonProperty("mentionable")
    val mentionable: Boolean? = null
)
