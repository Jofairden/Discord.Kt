package com.jofairden.discordkt.model.gateway

import com.fasterxml.jackson.annotation.JsonProperty

data class GatewayUrl(
    @JsonProperty("url")
    val url: String
)
