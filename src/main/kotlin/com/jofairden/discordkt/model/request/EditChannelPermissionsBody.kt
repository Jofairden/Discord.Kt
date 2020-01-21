package com.jofairden.discordkt.model.request

import com.fasterxml.jackson.annotation.JsonProperty

data class EditChannelPermissionsBody(
    @JsonProperty
    val allow: Int,
    @JsonProperty
    val deny: Int,
    @JsonProperty
    val type: String
)
