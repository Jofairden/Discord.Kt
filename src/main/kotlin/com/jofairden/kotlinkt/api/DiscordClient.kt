package com.jofairden.kotlinkt.api

import com.fasterxml.jackson.databind.JsonNode
import com.jofairden.kotlinkt.model.DiscordClientProperties
import com.jofairden.kotlinkt.model.OpCode
import com.jofairden.kotlinkt.model.gateway.payload.GatewayPayload
import com.jofairden.kotlinkt.model.gateway.payload.HeartbeatPayload
import com.jofairden.kotlinkt.model.gateway.payload.IdentifyPayload
import com.jofairden.kotlinkt.util.JsonUtil
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import mu.KotlinLogging

class DiscordClient {

    private val logger = KotlinLogging.logger { }
    private val internalClient = InternalClient(this)

    fun connect(properties: DiscordClientProperties) {
        internalClient.connect(properties)
    }

    internal inner class Events {
        private var sessionId: Int? = null

        fun ready(node: JsonNode) {
            logger.info { JsonUtil.Mapper.writeValueAsString(node) }
            sessionId = node["session_id"].asInt()
        }
    }

    internal inner class GatewayGuardian {

        private var sequenceNumber: Int? = null

        private val events = Events()

        fun dispatch(node: JsonNode) {
            sequenceNumber = node["s"].asInt()
            when (node["t"].asText()) {
                "READY" -> events.ready(node["d"])
            }
        }

        /**
         * When we receive the hello dispatch we should start sending heartbeats and an identify payload
         */
        fun hello(node: JsonNode, properties: DiscordClientProperties) {
            val interval = node["d"]["heartbeat_interval"].asInt()

            // TODO hbJobScope
            GlobalScope.launch {
                while (this.isActive) {
                    with(internalClient) {
                        GatewayPayload(
                            OpCode.Heartbeat.code,
                            HeartbeatPayload(sequenceNumber).toJsonNode()
                        ).send()
                    }
                    delay(interval.toLong())
                }
            }

            with(internalClient) {
                GatewayPayload(
                    OpCode.Identify.code,
                    IdentifyPayload(properties.token).toJsonNode()
                ).send()
            }
        }
    }
}
