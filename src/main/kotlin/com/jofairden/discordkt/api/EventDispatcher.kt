package com.jofairden.discordkt.api

import com.fasterxml.jackson.databind.JsonNode
import com.jofairden.discordkt.model.context.event.ReadyEventContext
import com.jofairden.discordkt.model.gateway.GatewayEvent
import com.jofairden.discordkt.util.JsonUtil
import mu.KotlinLogging

internal class EventDispatcher(
    private val discordClient: DiscordClient
) {
    private val logger = KotlinLogging.logger {}

    suspend fun dispatch(event: GatewayEvent, node: JsonNode) {
        when (event) {
            GatewayEvent.Hello -> {
            }
            GatewayEvent.Ready -> {
                discordClient.sessionId = node["session_id"].asInt()
                val ctx = JsonUtil.Mapper.treeToValue(node, ReadyEventContext::class.java)
                discordClient.ready(ctx)
            }
            GatewayEvent.Resumed -> {
            }
            GatewayEvent.Reconnect -> {
            }
            GatewayEvent.InvalidSession -> {
            }
            GatewayEvent.ChannelCreate -> {
            }
            GatewayEvent.ChannelUpdate -> {
            }
            GatewayEvent.ChannelDelete -> {
            }
            GatewayEvent.ChannelPinsUpdate -> {
            }
            GatewayEvent.GuildCreate -> {
            }
            GatewayEvent.GuildUpdate -> {
            }
            GatewayEvent.GuildDelete -> {
            }
            GatewayEvent.GuildBanAdd -> {
            }
            GatewayEvent.GuildBanRemove -> {
            }
            GatewayEvent.GuildEmojisUpdate -> {
            }
            GatewayEvent.GuildIntegrationsUpdate -> {
            }
            GatewayEvent.GuildMemberAdd -> {
            }
            GatewayEvent.GuildMemberRemove -> {
            }
            GatewayEvent.GuildMemberUpdate -> {
            }
            GatewayEvent.GuildMembersChunk -> {
            }
            GatewayEvent.GuildRoleCreate -> {
            }
            GatewayEvent.GuildRoleUpdate -> {
            }
            GatewayEvent.GuildRoleDelete -> {
            }
            GatewayEvent.MessageCreate -> {
            }
            GatewayEvent.MessageUpdate -> {
            }
            GatewayEvent.MessageDelete -> {
            }
            GatewayEvent.MessageDeleteBulk -> {
            }
            GatewayEvent.MessageReactionAdd -> {
            }
            GatewayEvent.MessageReactionRemove -> {
            }
            GatewayEvent.MessageReactionRemoveAll -> {
            }
            GatewayEvent.PresenceUpdate -> {
            }
            GatewayEvent.TypingStart -> {
            }
            GatewayEvent.UserUpdate -> {
            }
            GatewayEvent.VoiceStateUpdate -> {
            }
            GatewayEvent.VoiceServerUpdate -> {
            }
            GatewayEvent.WebhooksUpdate -> {
            }
        }
    }
}
