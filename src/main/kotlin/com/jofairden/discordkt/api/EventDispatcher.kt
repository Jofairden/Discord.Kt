package com.jofairden.discordkt.api

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ObjectNode
import com.jofairden.discordkt.model.context.event.ReadyEventContext
import com.jofairden.discordkt.model.discord.guild.UnavailableGuild
import com.jofairden.discordkt.model.gateway.GatewayEvent
import com.jofairden.discordkt.util.JsonUtil
import mu.KotlinLogging

internal class EventDispatcher(
    private val discordClient: DiscordClient
) {
    private val logger = KotlinLogging.logger {}

    private inline fun <reified T> parseNode(node: JsonNode): T =
        JsonUtil.Mapper.treeToValue<T>(node, T::class.java)

    suspend fun dispatch(event: GatewayEvent, node: JsonNode) {
        logger.info { "Event being dispatched: ${event.name}" }

        with(discordClient) {
            when (event) {
                GatewayEvent.Hello -> {
                }
                GatewayEvent.Ready -> {
                    sessionId = node["session_id"].asInt()
                    val ctx = parseNode<ReadyEventContext>(node)
                    ctx.guilds.forEach {
                        val id = it.id.toLong()
                        dataCache.guilds.put(id, serviceProvider.guildService.getGuild(id))
                    }
                    readyEventHandlers.forEach { it(ctx) }
                }
                GatewayEvent.Resumed -> {
                    resumedEventBlocks.forEach { it() }
                }
                GatewayEvent.Reconnect -> {
                }
                GatewayEvent.InvalidSession -> {
                    invalidSessionEventBlocks.forEach { it(node.asBoolean()) }
                }
                GatewayEvent.ChannelCreate -> {
                    channelUpdateEventBlocks.forEach { it(node) }
                }
                GatewayEvent.ChannelUpdate -> {
                    channelUpdateEventBlocks.forEach { it(node) }
                }
                GatewayEvent.ChannelDelete -> {
                    channelDeleteEventBlocks.forEach { it(node) }
                }
                GatewayEvent.ChannelPinsUpdate -> {
                    channelPinsUpdateEventBlocks.forEach { it(parseNode(node)) }
                }
                GatewayEvent.GuildCreate -> {
                    guildCreateEventBlocks.forEach { it(parseNode(node)) }
                }
                GatewayEvent.GuildUpdate -> {
                    guildUpdateEventBlocks.forEach { it(parseNode(node)) }
                }
                GatewayEvent.GuildDelete -> {
                    val ctx = parseNode<UnavailableGuild>(node)
                    guildDeleteEventBlocks.forEach { it(ctx, !ctx.unavailable) }
                }
                GatewayEvent.GuildBanAdd -> {
                    guildBanAddEventBlocks.forEach { it(parseNode(node)) }
                }
                GatewayEvent.GuildBanRemove -> {
                    guildBanRemoveEventBlocks.forEach { it(parseNode(node)) }
                }
                GatewayEvent.GuildEmojisUpdate -> {
                    guildEmojisUpdateEventBlocks.forEach { it(parseNode(node)) }
                }
                GatewayEvent.GuildIntegrationsUpdate -> {
                    guildIntegrationsUpdateEventBlocks.forEach { it(parseNode(node)) }
                }
                GatewayEvent.GuildMemberAdd -> {
                    // Strip extra guild_id field (WHY DISCORD?)
                    guildMemberAddEventBlocks.forEach { it(parseNode((node as ObjectNode).remove("guild_id"))) }
                }
                GatewayEvent.GuildMemberRemove -> {
                    guildMemberRemoveEventBlocks.forEach { it(parseNode(node)) }
                }
                GatewayEvent.GuildMemberUpdate -> {
                    guildMemberUpdateEventBlocks.forEach { it(parseNode(node)) }
                }
                GatewayEvent.GuildMembersChunk -> {
                    // TODO store info
                    guildMembersChunkEventBlocks.forEach { it(parseNode(node)) }
                }
                GatewayEvent.GuildRoleCreate -> {
                    guildRoleCreateEventBlocks.forEach { it(parseNode(node)) }
                }
                GatewayEvent.GuildRoleUpdate -> {
                    guildRoleUpdateEventBlocks.forEach { it(parseNode(node)) }
                }
                GatewayEvent.GuildRoleDelete -> {
                    guildRoleDeleteEventBlocks.forEach { it(parseNode(node)) }
                }
                GatewayEvent.MessageCreate -> {
                    messageCreateEventBlocks.forEach { it(parseNode(node)) }
                }
                GatewayEvent.MessageUpdate -> {
                    messageUpdateEventBlocks.forEach { it(parseNode(node)) }
                }
                GatewayEvent.MessageDelete -> {
                    messageDeleteEventBlocks.forEach { it(parseNode(node)) }
                }
                GatewayEvent.MessageDeleteBulk -> {
                    messageDeleteBulkEventBlocks.forEach { it(parseNode(node)) }
                }
                GatewayEvent.MessageReactionAdd -> {
                    messageReactionAddEventBlocks.forEach { it(parseNode(node)) }
                }
                GatewayEvent.MessageReactionRemove -> {
                    messageReactionRemoveEventBlocks.forEach { it(parseNode(node)) }
                }
                GatewayEvent.MessageReactionRemoveAll -> {
                    messageReactionRemoveAllEventBlocks.forEach { it(parseNode(node)) }
                }
                GatewayEvent.PresenceUpdate -> {
                    // TODO
                }
                GatewayEvent.TypingStart -> {
                    typingStartEventBlocks.forEach { it(parseNode(node)) }
                }
                GatewayEvent.UserUpdate -> {
                    userUpdateEventBlocks.forEach { it(parseNode(node)) }
                }
                GatewayEvent.VoiceStateUpdate -> {
                    // TODO
                }
                GatewayEvent.VoiceServerUpdate -> {
                    // TODO
                }
                GatewayEvent.WebhooksUpdate -> {
                    // TODO
                }
            }
        }
    }
}
