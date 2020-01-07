package com.jofairden.kotlinkt.api

import com.fasterxml.jackson.databind.JsonNode
import com.jofairden.kotlinkt.model.api.DiscordClientProperties
import com.jofairden.kotlinkt.model.context.event.ReadyEventContext
import com.jofairden.kotlinkt.model.gateway.GatewayEvent
import com.jofairden.kotlinkt.model.gateway.OpCode
import com.jofairden.kotlinkt.model.gateway.payload.GatewayPayload
import com.jofairden.kotlinkt.model.gateway.payload.HeartbeatPayload
import com.jofairden.kotlinkt.model.gateway.payload.IdentifyPayload
import com.jofairden.kotlinkt.model.gateway.payload.ResumePayload
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import mu.KotlinLogging

class DiscordClient {

    companion object Builder {
        fun build(block: DiscordClient.() -> Unit) = DiscordClient().also(block)
        fun buildAndRun(token: String, block: DiscordClient.() -> Unit) = build(block).also {
            it.connect(
                DiscordClientProperties(token)
            )
        }
    }

    private val logger = KotlinLogging.logger { }
    private val internalClient = InternalClient(this)
    internal lateinit var properties: DiscordClientProperties
    internal var sessionId: Int? = null
    internal var sequenceNumber: Int? = null

    fun connect(properties: DiscordClientProperties) {
        this.properties = properties
        internalClient.connect()
    }

    //MutableList<suspend (ctx : T) -> Unit> where T : EventContext
    private val readyEventHandlers: MutableList<ReadyEventBlock> = ArrayList()

    fun onReady(block: ReadyEventBlock) {
        readyEventHandlers += block
    }

    suspend fun ready(ctx: ReadyEventContext) {
        readyEventHandlers.forEach { it(ctx) }
    }

    internal inner class GatewayGuardian(
        private val eventDispatcher: EventDispatcher
    ) {
        private var isReconnecting = false
        private var hbJob: Job? = null

        /**
         * Receive an event dispatch
         */
        suspend fun dispatch(node: JsonNode) {
            sequenceNumber = node["s"].asInt()
            when (node["t"].asText()) {
                "READY" -> {
                    if (isReconnecting && sequenceNumber != null) {
                        resume()
                    }
                    eventDispatcher.dispatch(GatewayEvent.Ready, node["d"])
                }
            }
        }

        // TODO track last hb and ack (determine zombied connection)
        fun heartbeatAck() {
            logger.info { "Heartbeat Acknowledged" }
        }

        /**
         * Attempt to send a Heartbeat OpCode with a payload consisting of the last known sequence number
         */
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
         * The HELLO dispatch contains an interval at which we should send heartbeats
         * The basic Identify payload consists of the bot token
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

        /**
         * Attempt to resume the connection
         * Consists of sending the Resume OpCode with a payload consisting of the bot token, sessionId and sequence number
         */
        private fun resume() {
            with(internalClient) {
                GatewayPayload(
                    OpCode.Resume.code,
                    ResumePayload(properties.token, sessionId.toString(), sequenceNumber!!).toJsonNode()
                ).send()
            }
        }

        /**
         * Attempts to handle an invalid sessions
         */
        fun invalidSession(node: JsonNode) {
            if (node["d"].asBoolean()) reconnect()
            else disconnect()
        }

        /**
         * Disconnect the current client
         */
        private fun disconnect() {
            internalClient.disconnect()
        }

        /**
         * Attempt to reconnect with Discord
         * Reconnection consists of disconnecting the current client, then connecting as normal
         * After being reconnected and receiving the READY event, the client will attempt to send a Resume OpCode
         */
        fun reconnect() {
            disconnect()
            isReconnecting = true
            connect(properties)
        }
    }
}
