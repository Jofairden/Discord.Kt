package com.jofairden.discordkt.api

import com.jofairden.discordkt.api.cache.DataCache
import com.jofairden.discordkt.model.gateway.GatewayCloseEventCode
import com.jofairden.discordkt.model.gateway.OpAction
import com.jofairden.discordkt.model.gateway.OpCode
import com.jofairden.discordkt.model.gateway.payload.GatewayPayload
import com.jofairden.discordkt.util.send
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import org.koin.core.context.startKoin
import org.koin.dsl.module

internal class InternalDiscordClient(
    private val discordClient: DiscordClient
) {
    private val logger = KotlinLogging.logger { }
    private val eventDispatcher = EventDispatcher(discordClient)
    private val messageDispatcher = MessageDispatcher(discordClient, eventDispatcher)
    private val discordWsListener = DiscordWsListener(messageDispatcher)
    private lateinit var webSocket: WebSocket
    private lateinit var client: OkHttpClient

    fun connect() {
        client = OkHttpClient.Builder()
            .authenticator(DiscordClientAuthenticator(discordClient.properties))
            .addInterceptor(DiscordClientChainInterceptor(discordClient.properties))
            .build()
        // Inject client into Retrofit SP
        discordClient.serviceProvider = ApiServiceProvider(client)
        discordClient.dataCache = DataCache(discordClient.properties.dataCacheProperties, discordClient.serviceProvider)

        // Start DI instance
        startKoin {
            modules(module {
                single { discordClient.serviceProvider }
                single { discordClient.dataCache }
            })
        }

        logger.info { "Connecting to Discord..." }
        createWebsocket()
        logger.info { "Connected to ${webSocket.request().url}" }
    }

    fun disconnect(reason: String? = null) {
        webSocket.close(GatewayCloseEventCode.UnknownError.code, reason)
    }

    fun GatewayPayload.send() {
        val opCode = OpCode.find(this.op) ?: throw RuntimeException("OpCode ${this.op} not found")
        if (opCode.action == OpAction.Receive) {
            throw RuntimeException("Impossible to send a gateway payload with an opcode that may only be received")
        }
        logger.info { "Sending OpCode ${this.op}" }
        webSocket.send(this)
    }

    private fun createWebsocket() = runBlocking(Dispatchers.IO) {
        val url = discordClient.serviceProvider.gatewayService.getGateway().url
        val req = Request.Builder().url("$url?v=6&encoding=json").build()
        webSocket = client.newWebSocket(req, discordWsListener)
    }
}
