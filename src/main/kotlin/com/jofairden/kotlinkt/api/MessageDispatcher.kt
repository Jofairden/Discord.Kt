package com.jofairden.kotlinkt.api

import com.jofairden.kotlinkt.model.DiscordClientProperties
import com.jofairden.kotlinkt.model.OpCode
import com.jofairden.kotlinkt.util.JsonUtil
import mu.KotlinLogging

internal class MessageDispatcher(
    private val discordClient: DiscordClient
) {

    private val logger = KotlinLogging.logger { }
    private val guardian = discordClient.GatewayGuardian()
    lateinit var properties: DiscordClientProperties

    fun dispatch(text: String) {
        val node = JsonUtil.Mapper.readTree(text)
        val opCode = OpCode::class.sealedSubclasses
            .firstOrNull { node["op"].asInt() == it.objectInstance?.code }
            ?.objectInstance

        if (opCode == null) {
            logger.error { "Impossible opCode: ${node["op"].asInt()}" }
            return
        }

        when (opCode) {
            OpCode.Dispatch -> guardian.dispatch(node)
            OpCode.Heartbeat -> {
            }
            OpCode.Identify -> {
            }
            OpCode.StatusUpdate -> {
            }
            OpCode.VoiceStateUpdate -> {
            }
            OpCode.Resume -> {
            }
            OpCode.Reconnect -> {
            }
            OpCode.RequestGuildMembers -> {
            }
            OpCode.InvalidSession -> {
            }
            OpCode.Hello -> guardian.hello(node, properties)
            OpCode.HeartbeatACK -> {
                logger.info { "heartbeat ack" }
            }
        }
    }
}
