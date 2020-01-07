package com.jofairden.kotlinkt.model.context.event

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.JsonNode

class ReadyEventContext(
    @JsonProperty("v")
    val gatewayProtocolVersion: Int,

    @JsonProperty("user_settings")
    val botUserSettings: JsonNode,

    @JsonProperty("user")
    val botUser: JsonNode,

    @JsonProperty("session_id")
    val sessionId: String,

    @JsonProperty("relationships")
    val relationShips: Array<JsonNode>,

    @JsonProperty("private_channels")
    val privateChannels: Array<JsonNode>,

    @JsonProperty("presences")
    val presences: Array<JsonNode>,

    @JsonProperty("guilds")
    val guilds: Array<JsonNode>,

    @JsonProperty("application")
    val application: JsonNode,

    @JsonProperty("shard")
    val shard: ArrayList<Int>?,

    @JsonProperty("_trace")
    @JsonIgnore
    val trace: JsonNode
)
