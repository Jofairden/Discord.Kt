package com.jofairden.kotlinkt.api

import com.jofairden.kotlinkt.model.GatewayCloseEventCode
import com.jofairden.kotlinkt.model.OpAction
import com.jofairden.kotlinkt.model.OpCode
import com.jofairden.kotlinkt.model.gateway.payload.GatewayPayload
import com.jofairden.kotlinkt.util.JsonUtil
import mu.KotlinLogging
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

internal class InternalClient(
    discordClient: DiscordClient
) {

    companion object {
        private val WS_REQUEST = Request.Builder()
            .url("wss://gateway.discord.gg/?v=6&encoding=json")
            .build()
    }

    private val logger = KotlinLogging.logger { }
    private val dispatcher = MessageDispatcher(discordClient)

    internal val client = OkHttpClient()
    internal val webSocket: WebSocket by lazy {
        client.newWebSocket(
            WS_REQUEST,
            object : WebSocketListener() {
                override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                    logger.trace { "Websocket closed" }
                }

                override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                    logger.trace { "Websocket closing" }
                }

                override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                    logger.trace { "Websocket failed" }
                    logger.error { t.localizedMessage }
                }

                override fun onOpen(webSocket: WebSocket, response: Response) {
                    logger.trace { "Websocket opened" }
                }

                override fun onMessage(webSocket: WebSocket, text: String) {
                    dispatcher.dispatch(text)
                }
            })
    }

    fun connect() {
        logger.info { "Starting websocket ${webSocket.request().url}" }
    }

    fun disconnect(reason: String? = null) {
        webSocket.close(GatewayCloseEventCode.UnknownError.code, reason)
    }

    fun GatewayPayload.send() {
        if (OpCode.find(this.op)?.action == OpAction.Receive) {
            throw RuntimeException("Impossible to send a gateway payload with an opcode that may only be received")
        }
        webSocket.send(JsonUtil.Mapper.writeValueAsString(this))
    }
}
