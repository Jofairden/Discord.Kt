package com.jofairden.kotlinkt.model.gateway.payload

import com.fasterxml.jackson.annotation.JsonProperty

data class IdentifyPayload(
    val token: String,
    val properties: IdentifyPayloadProps = IdentifyPayloadProps()
) : WsPayload()

data class IdentifyPayloadProps(
    @JsonProperty("\$os")
    val os: String = "jvm",
    @JsonProperty("\$browser")
    val browser: String = "Discord.Kt",
    @JsonProperty("\$device")
    val device: String = "Diskord.Kt"
)


