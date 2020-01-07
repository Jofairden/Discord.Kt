package com.jofairden.discordkt.model.gateway.payload

import com.fasterxml.jackson.databind.JsonNode
import com.jofairden.discordkt.util.JsonUtil

abstract class WebsocketPayload {
    fun toJsonNode() = JsonUtil.Mapper.valueToTree<JsonNode>(this)
}
