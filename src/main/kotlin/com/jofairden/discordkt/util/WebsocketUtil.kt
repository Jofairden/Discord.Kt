package com.jofairden.discordkt.util

import com.jofairden.discordkt.model.gateway.payload.GatewayPayload
import okhttp3.WebSocket

internal fun WebSocket.send(payload: GatewayPayload) {
    send(JsonUtil.Mapper.writeValueAsString(payload))
}
