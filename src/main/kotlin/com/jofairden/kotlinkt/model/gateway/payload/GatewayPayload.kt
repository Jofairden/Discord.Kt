package com.jofairden.kotlinkt.model.gateway.payload

import com.fasterxml.jackson.databind.JsonNode

data class GatewayPayload(
    val op: Int,
    val d: JsonNode,
    val s: Int? = null,
    val t: String? = null
)
