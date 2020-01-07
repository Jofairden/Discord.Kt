package com.jofairden.kotlinkt.api

import mu.KotlinLogging
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

internal class DiscordWsListener(
    private val dispatcher: MessageDispatcher
) : WebSocketListener() {

    private val logger = KotlinLogging.logger { }

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
}
