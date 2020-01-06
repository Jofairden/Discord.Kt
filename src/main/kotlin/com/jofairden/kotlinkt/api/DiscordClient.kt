package com.jofairden.kotlinkt.api

import com.fasterxml.jackson.databind.JsonNode
import com.jofairden.kotlinkt.model.DiscordClientProperties
import com.jofairden.kotlinkt.model.OpCode
import com.jofairden.kotlinkt.model.gateway.payload.GatewayPayload
import com.jofairden.kotlinkt.model.gateway.payload.HeartbeatPayload
import com.jofairden.kotlinkt.model.gateway.payload.IdentifyPayload
import com.jofairden.kotlinkt.model.gateway.payload.ResumePayload
import com.jofairden.kotlinkt.util.JsonUtil
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import mu.KotlinLogging

class DiscordClient {

    private val logger = KotlinLogging.logger { }
    private val internalClient = InternalClient(this)
    internal lateinit var properties: DiscordClientProperties

    fun connect(properties: DiscordClientProperties) {
        this.properties = properties
        internalClient.connect()
    }

    internal inner class Events {
        internal var sessionId: Int? = null

        fun ready(node: JsonNode) {
            logger.info { JsonUtil.Mapper.writeValueAsString(node) }
            sessionId = node["session_id"].asInt()
        }
    }

    internal inner class GatewayGuardian {

        private var isReconnecting = false
        private var sequenceNumber: Int? = null
        private var hbJob: Job? = null
        private val events = Events()

        fun dispatch(node: JsonNode) {
            sequenceNumber = node["s"].asInt()
            when (node["t"].asText()) {
                "READY" -> {
                    if (isReconnecting && sequenceNumber != null) {
                        resume()
                    }
                    events.ready(node["d"])
                }
            }
        }

        // TODO track last hb and ack (determine zombied connection)
        fun heartbeatAck() {
            logger.info { "Heartbeat Ack" }
        }

        fun heartbeat() {
            with(internalClient) {
                GatewayPayload(
                    OpCode.Heartbeat.code,
                    HeartbeatPayload(sequenceNumber).toJsonNode()
                ).send()
            }
        }

        /**
         * When we receive the hello dispatch we should start sending heartbeats and an identify payload
         */
        fun hello(node: JsonNode) {
            val interval = node["d"]["heartbeat_interval"].asInt()

            hbJob?.cancel()
            // TODO hbJobScope
            hbJob = GlobalScope.launch {
                while (this.isActive) {
                    heartbeat()
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

        private fun resume() {
            with(internalClient) {
                GatewayPayload(
                    OpCode.Resume.code,
                    ResumePayload(properties.token, events.sessionId.toString(), sequenceNumber!!).toJsonNode()
                ).send()
            }
        }

        fun reconnect() {
            internalClient.disconnect()
            isReconnecting = true
            connect(properties)
        }
    }
}
