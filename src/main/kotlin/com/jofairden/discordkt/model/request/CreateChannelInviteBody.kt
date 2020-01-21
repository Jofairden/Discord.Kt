package com.jofairden.discordkt.model.request

import com.fasterxml.jackson.annotation.JsonProperty

data class CreateChannelInviteBody(
    @JsonProperty("max_age")
    val maxAgeInSeconds: Int? = null,
    @JsonProperty("max_uses")
    val maxUses: Int? = null,
    @JsonProperty("temporary")
    val temporary: Boolean? = null,
    @JsonProperty("unique")
    val unique: Boolean? = null
)
