package com.jofairden.discordkt.api

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ObjectNode
import com.jofairden.discordkt.api.cache.CombinedId
import com.jofairden.discordkt.api.cache.getSuspending
import com.jofairden.discordkt.model.context.event.ChannelCreateEventContext
import com.jofairden.discordkt.model.context.event.GuildMemberAddEventContext
import com.jofairden.discordkt.model.context.event.ReadyEventContext
import com.jofairden.discordkt.model.discord.channel.DiscordChannel
import com.jofairden.discordkt.model.discord.guild.Guild
import com.jofairden.discordkt.model.discord.guild.GuildUser
import com.jofairden.discordkt.model.discord.guild.UnavailableGuild
import com.jofairden.discordkt.model.discord.message.DiscordMessage
import com.jofairden.discordkt.model.discord.message.DiscordMessageUpdate
import com.jofairden.discordkt.model.gateway.GatewayEvent
import com.jofairden.discordkt.util.JsonUtil
import mu.KotlinLogging

internal class EventDispatcher(
    private val client: DiscordClient
) {
    private val logger = KotlinLogging.logger {}

    private inline fun <reified T> parseNode(node: JsonNode): T =
        JsonUtil.Mapper.treeToValue<T>(node, T::class.java)

    suspend fun dispatch(event: GatewayEvent, node: JsonNode) {
        logger.info { "Event being dispatched: ${event.name}" }

        with(client) {
            when (event) {
                GatewayEvent.Hello -> {
                }
                GatewayEvent.Ready -> {
                    val ctx = parseNode<ReadyEventContext>(node)
                    sessionId = ctx.sessionId
                    botUser = ctx.botUser

                    ctx.guilds.forEach { g ->
                        val guild = dataCache.guildCache.getSuspending(g.id)
                        logger.info { "Loaded guild ${guild.id}" }
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
                    val channel = parseNode<DiscordChannel>(node)
                    dataCache.cacheChannel(channel)
                    channelCreateEventBlocks.forEach { it(ChannelCreateEventContext(client, channel)) }
                }
                GatewayEvent.ChannelUpdate -> {
                    val channel = parseNode<DiscordChannel>(node)
                    dataCache.cacheChannel(channel)
                    channelUpdateEventBlocks.forEach { it(channel) }
                }
                GatewayEvent.ChannelDelete -> {
                    channelDeleteEventBlocks.forEach { it(parseNode(node)) }
                }
                GatewayEvent.ChannelPinsUpdate -> {
                    channelPinsUpdateEventBlocks.forEach { it(parseNode(node)) }
                }
                GatewayEvent.GuildCreate -> {
                    val guild = parseNode<Guild>(node)
                    dataCache.cacheGuild(guild)
                    guildCreateEventBlocks.forEach { it(guild) }
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
                    val guildId = node.get("guild_id").asLong()
                    // Strip extra guild_id field and append to ctx (WHY DISCORD?)
                    (node as ObjectNode).remove("guild_id")
                    val ctxNode = parseNode<GuildUser>(node).run {
                        GuildMemberAddEventContext(this, dataCache).also { it.guildId = guildId }
                    }
                    guildMemberAddEventBlocks.forEach { it(ctxNode) }
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
                    var message = parseNode<DiscordMessage>(node)
                    message = dataCache.enrich(message)
                    dataCache.cacheMessage(message)
                    messageCreateEventBlocks.forEach { it(message) }
                }
                GatewayEvent.MessageUpdate -> {
                    val update = parseNode<DiscordMessageUpdate>(node)
                    var message = dataCache.messageCache.getSuspending(CombinedId(update.channelId, update.id))
                    message = message.copyFromMessageUpdate(update)
                    message = dataCache.enrich(message)
                    dataCache.cacheMessage(message)
                    messageUpdateEventBlocks.forEach { it(message) }
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
