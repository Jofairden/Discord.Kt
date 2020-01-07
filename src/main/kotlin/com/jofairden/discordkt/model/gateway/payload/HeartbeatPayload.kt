package com.jofairden.discordkt.model.gateway.payload

data class HeartbeatPayload(
    val s: Int?
) : WebsocketPayload()
