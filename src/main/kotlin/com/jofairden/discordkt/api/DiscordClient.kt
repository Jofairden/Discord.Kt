package com.jofairden.discordkt.api

import com.fasterxml.jackson.databind.JsonNode
import com.jofairden.discordkt.api.cache.DataCache
import com.jofairden.discordkt.model.api.DiscordClientProperties
import com.jofairden.discordkt.model.discord.user.DiscordUser
import com.jofairden.discordkt.model.discord.user.UserStatus
import com.jofairden.discordkt.model.discord.user.UserStatusActivity
import com.jofairden.discordkt.model.gateway.GatewayEvent
import com.jofairden.discordkt.model.gateway.OpCode
import com.jofairden.discordkt.model.gateway.payload.GatewayPayload
import com.jofairden.discordkt.model.gateway.payload.HeartbeatPayload
import com.jofairden.discordkt.model.gateway.payload.IdentifyPayload
import com.jofairden.discordkt.model.gateway.payload.ResumePayload
import com.jofairden.discordkt.model.gateway.payload.UpdateStatusPayload
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import mu.KotlinLogging

@DslMarker
annotation class DiscordClientDsl

class DiscordClient {

    companion object {
        fun build(block: DiscordClient.() -> Unit) = DiscordClient().also(block)
        fun buildAndRun(token: String, block: DiscordClient.() -> Unit) = build(block).also {
            it.connect(
                DiscordClientProperties(token)
            )
        }
    }

    lateinit var botUser: DiscordUser
        internal set

    private val logger = KotlinLogging.logger { }
    private val internalClient = InternalDiscordClient(this)
    internal lateinit var dataCache: DataCache
    internal lateinit var properties: DiscordClientProperties
    internal lateinit var serviceProvider: ApiServiceProvider
    internal var sessionId: String? = null
    internal var sequenceNumber: Int? = null

    fun connect(properties: DiscordClientProperties) {
        this.properties = properties
        internalClient.connect()
    }

    internal val readyEventHandlers: MutableList<ReadyEventBlock> = arrayListOf()
    internal val resumedEventBlocks: MutableList<NoArgsEventBlock> = arrayListOf()
    internal val invalidSessionEventBlocks: MutableList<InvalidSessionEventBlock> = arrayListOf()
    internal val channelCreateEventBlocks: MutableList<ChannelCreateEventBlock> = arrayListOf()
    internal val channelUpdateEventBlocks: MutableList<ChannelEventBlock> = arrayListOf()
    internal val channelDeleteEventBlocks: MutableList<ChannelEventBlock> = arrayListOf()
    internal val channelPinsUpdateEventBlocks: MutableList<ChannelPinsUpdateEventBlock> = arrayListOf()
    internal val guildCreateEventBlocks: MutableList<GuildEventBlock> = arrayListOf()
    internal val guildUpdateEventBlocks: MutableList<GuildEventBlock> = arrayListOf()
    internal val guildDeleteEventBlocks: MutableList<GuildDeleteEventBlock> = arrayListOf()
    internal val guildBanAddEventBlocks: MutableList<GuildBanEventBlock> = arrayListOf()
    internal val guildBanRemoveEventBlocks: MutableList<GuildBanEventBlock> = arrayListOf()
    internal val guildEmojisUpdateEventBlocks: MutableList<GuildEmojisUpdateEventBlock> = arrayListOf()
    internal val guildIntegrationsUpdateEventBlocks: MutableList<GuildIdEventBlock> = arrayListOf()
    internal val guildMemberAddEventBlocks: MutableList<GuildMemberAddEventBlock> = arrayListOf()
    internal val guildMemberRemoveEventBlocks: MutableList<GuildMemberRemoveEventBlock> = arrayListOf()
    internal val guildMemberUpdateEventBlocks: MutableList<GuildMemberUpdateEventBlock> = arrayListOf()
    // TODO should this be exposed?
    internal val guildMembersChunkEventBlocks: MutableList<GuildMembersChunkEventBlock> = arrayListOf()
    internal val guildRoleCreateEventBlocks: MutableList<GuildRoleCreateEventBlock> = arrayListOf()
    internal val guildRoleUpdateEventBlocks: MutableList<GuildRoleUpdateEventBlock> = arrayListOf()
    internal val guildRoleDeleteEventBlocks: MutableList<GuildRoleDeleteEventBlock> = arrayListOf()
    internal val messageCreateEventBlocks: MutableList<MessageCreateEventBlock> = arrayListOf()
    internal val messageUpdateEventBlocks: MutableList<MessageUpdateEventBlock> = arrayListOf()
    internal val messageDeleteEventBlocks: MutableList<MessageDeleteEventBlock> = arrayListOf()
    internal val messageDeleteBulkEventBlocks: MutableList<MessageDeleteBulkEventBlock> = arrayListOf()
    internal val messageReactionAddEventBlocks: MutableList<MessageReactionAddEventBlock> = arrayListOf()
    internal val messageReactionRemoveEventBlocks: MutableList<MessageReactionRemoveEventBlock> = arrayListOf()
    internal val messageReactionRemoveAllEventBlocks: MutableList<MessageReactionRemoveAllEventBlock> = arrayListOf()
    internal val typingStartEventBlocks: MutableList<TypingStartEventBlock> = arrayListOf()
    internal val userUpdateEventBlocks: MutableList<UserUpdateEventBlock> = arrayListOf()

    suspend fun updateBotUser() =
        serviceProvider.userService.getBotUser().also {
            botUser = it
        }

    suspend fun updateBotUser(name: String) =
        serviceProvider.userService.modifyBotUser(name).also {
            botUser = it
        }

    suspend fun getGatewayUrl(): String =
        serviceProvider.gatewayService.getGateway().url

    fun updateStatus(
        idleTime: Int? = null,
        activity: UserStatusActivity? = null,
        status: UserStatus? = null,
        isAfk: Boolean = false
    ) {
        with(internalClient) {
            GatewayPayload(
                OpCode.StatusUpdate.code,
                UpdateStatusPayload(
                    idleTime,
                    activity,
                    status,
                    isAfk
                ).toJsonNode()
            ).send()
        }
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
            val event = GatewayEvent.find(node["t"].asText()) ?: return
            if (event == GatewayEvent.Ready && isReconnecting && sequenceNumber != null) {
                resume()
            }
            eventDispatcher.dispatch(event, node["d"])
        }

        // TODO track last hb and ack (determine zombied connection)
        fun heartbeatAck() {
            logger.info { "Heartbeat Acknowledged" }
        }

        /**
         * Attempt to send a Heartbeat OpCode with a payload consisting of the last known sequence number
         */
        fun heartbeat() {
            logger.info { "Sending heartbeat" }
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

            logger.info { "Identiyfing client" }
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
            logger.info { "Resuming connection" }
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
            logger.info { "Disconnecting client" }
            internalClient.disconnect()
        }

        /**
         * Attempt to reconnect with Discord
         * Reconnection consists of disconnecting the current client, then connecting as normal
         * After being reconnected and receiving the READY event, the client will attempt to send a Resume OpCode
         */
        fun reconnect() {
            logger.info { "Reconnecting client" }
            disconnect()
            isReconnecting = true
            connect(properties)
        }
    }
}
