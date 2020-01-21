package com.jofairden.discordkt.model.request

import com.fasterxml.jackson.annotation.JsonProperty

data class ModifyGuildRolePositionBody(
    @JsonProperty("id")
    val id: Long,
    @JsonProperty("position")
    val position: Int
)
