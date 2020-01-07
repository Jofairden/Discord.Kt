package com.jofairden.kotlinkt.model.gateway.payload

data class HeartbeatPayload(
    val s: Int?
) : WebsocketPayload()
