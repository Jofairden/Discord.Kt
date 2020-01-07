package com.jofairden.kotlinkt.api

import com.jofairden.kotlinkt.model.gateway.GatewayCloseEventCode
import com.jofairden.kotlinkt.model.gateway.OpAction
import com.jofairden.kotlinkt.model.gateway.OpCode
import com.jofairden.kotlinkt.model.gateway.payload.GatewayPayload
import com.jofairden.kotlinkt.util.RetrofitUtil
import com.jofairden.kotlinkt.util.send
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket

internal class InternalClient(
    discordClient: DiscordClient
) {
    private val logger = KotlinLogging.logger { }
    private val eventDispatcher = EventDispatcher(discordClient)
    private val messageDispatcher = MessageDispatcher(discordClient, eventDispatcher)
    private val discordWsListener = DiscordWsListener(messageDispatcher)

    internal val client = OkHttpClient()
    internal lateinit var webSocket: WebSocket

    fun connect() {
        logger.info { "Connecting to Discord..." }
        createWebsocket()
        logger.info { "Connected to ${webSocket.request().url}" }
    }

    fun disconnect(reason: String? = null) {
        webSocket.close(GatewayCloseEventCode.UnknownError.code, reason)
    }

    fun GatewayPayload.send() {
        if (OpCode.find(this.op)?.action == OpAction.Receive) {
            throw RuntimeException("Impossible to send a gateway payload with an opcode that may only be received")
        }
        webSocket.send(this)
    }

    private fun createWebsocket() = runBlocking(Dispatchers.IO) {
        val url = RetrofitUtil.gatewayService.getGateway()["url"].asText()
        val req = Request.Builder().url("$url?v=6&encoding=json").build()
        webSocket = client.newWebSocket(req, discordWsListener)
    }
}
