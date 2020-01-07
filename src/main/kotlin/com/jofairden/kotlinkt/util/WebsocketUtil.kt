package com.jofairden.kotlinkt.util

import com.jofairden.kotlinkt.model.gateway.payload.GatewayPayload
import okhttp3.WebSocket

fun WebSocket.send(payload: GatewayPayload) {
    send(JsonUtil.Mapper.writeValueAsString(payload))
}
