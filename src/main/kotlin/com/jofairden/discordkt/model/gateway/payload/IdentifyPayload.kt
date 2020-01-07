package com.jofairden.discordkt.model.gateway.payload

import com.fasterxml.jackson.annotation.JsonProperty

data class IdentifyPayload(
    val token: String,
    val properties: IdentifyPayloadProps = IdentifyPayloadProps()
) : WebsocketPayload()

data class IdentifyPayloadProps(
    @JsonProperty("\$os")
    val os: String = "jvm",
    @JsonProperty("\$browser")
    val browser: String = "Discord.Kt",
    @JsonProperty("\$device")
    val device: String = "Diskord.Kt"
)
