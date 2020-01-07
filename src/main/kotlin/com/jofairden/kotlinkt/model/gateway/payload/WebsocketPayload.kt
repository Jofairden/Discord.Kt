package com.jofairden.kotlinkt.model.gateway.payload

import com.fasterxml.jackson.databind.JsonNode
import com.jofairden.kotlinkt.util.JsonUtil

abstract class WebsocketPayload {
    fun toJsonNode() = JsonUtil.Mapper.valueToTree<JsonNode>(this)
}
