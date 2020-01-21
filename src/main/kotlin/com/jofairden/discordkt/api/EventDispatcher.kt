package com.jofairden.discordkt.api

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ObjectNode
import com.jofairden.discordkt.api.cache.CombinedId
import com.jofairden.discordkt.api.cache.getSuspending
import com.jofairden.discordkt.model.context.event.ChannelCreateEventContext
import com.jofairden.discordkt.model.context.event.GuildMemberAddEventContext
import com.jofairden.discordkt.model.context.event.GuildMemberRemoveEventContext
import com.jofairden.discordkt.model.context.event.GuildMemberUpdateEventContext
import com.jofairden.discordkt.model.context.event.GuildMembersChunkEventContext
import com.jofairden.discordkt.model.context.event.GuildRoleEventContext
import com.jofairden.discordkt.model.context.event.GuildRoleIdEventContext
import com.jofairden.discordkt.model.context.event.IEventContext
import com.jofairden.discordkt.model.context.event.MessageDeleteBulkEventContext
import com.jofairden.discordkt.model.context.event.MessageDeleteEventContext
import com.jofairden.discordkt.model.context.event.MessageReactionAddEventContext
import com.jofairden.discordkt.model.context.event.MessageReactionRemoveAllEventContext
import com.jofairden.discordkt.model.context.event.MessageReactionRemoveEventContext
import com.jofairden.discordkt.model.context.event.ReadyEventContext
import com.jofairden.discordkt.model.discord.channel.DiscordChannel
import com.jofairden.discordkt.model.discord.guild.Guild
import com.jofairden.discordkt.model.discord.guild.GuildUser
import com.jofairden.discordkt.model.discord.guild.UnavailableGuild
import com.jofairden.discordkt.model.discord.message.DiscordMessage
import com.jofairden.discordkt.model.discord.message.DiscordMessageUpdate
import com.jofairden.discordkt.model.discord.user.DiscordUser
import com.jofairden.discordkt.model.gateway.GatewayEvent
import com.jofairden.discordkt.util.JsonUtil
import mu.KotlinLogging

internal class EventDispatcher(
    private val client: DiscordClient
) {
    private val logger = KotlinLogging.logger {}

    private inline fun <reified T> parseNode(node: JsonNode): T =
        JsonUtil.Mapper.treeToValue<T>(node, T::class.java).also {
            if (it is IEventContext) {
                it.discordClient = client
            }
        }

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
                    channelCreateEventBlocks.forEach { it(ChannelCreateEventContext(channel)) }
                }
                GatewayEvent.ChannelUpdate -> {
                    val channel = parseNode<DiscordChannel>(node)
                    dataCache.cacheChannel(channel)
                    channelUpdateEventBlocks.forEach { it(channel) }
                }
                GatewayEvent.ChannelDelete -> {
                    val ctx = parseNode<DiscordChannel>(node)
                    dataCache.channelCache.synchronous().invalidate(ctx.id)
                    channelDeleteEventBlocks.forEach { it(ctx) }
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
                    dataCache.guildCache.synchronous().invalidate(ctx.id)
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
                    val user = parseNode<GuildUser>(node)
                    val ctx = GuildMemberAddEventContext(user).also { it.guildId = guildId }
                    guildMemberAddEventBlocks.forEach { it(ctx) }
                }
                GatewayEvent.GuildMemberRemove -> {
                    val ctx = parseNode<GuildMemberRemoveEventContext>(node)
                    dataCache.userCache.synchronous().invalidate(ctx.user.id)
                    guildMemberRemoveEventBlocks.forEach { it(ctx) }
                }
                GatewayEvent.GuildMemberUpdate -> {
                    val ctx = parseNode<GuildMemberUpdateEventContext>(node)
                    dataCache.guildMemberUpdate(ctx)
                    guildMemberUpdateEventBlocks.forEach { it(ctx) }
                }
                GatewayEvent.GuildMembersChunk -> {
                    // TODO store info
                    val ctx = parseNode<GuildMembersChunkEventContext>(node)
                    if (ctx.notFound == false) {
                        guildMembersChunkEventBlocks.forEach { it(ctx) }
                    }
                }
                GatewayEvent.GuildRoleCreate -> {
                    val ctx = parseNode<GuildRoleEventContext>(node)
                    dataCache.cacheGuildRole(ctx.guildId, ctx.role)
                    guildRoleCreateEventBlocks.forEach { it(ctx) }
                }
                GatewayEvent.GuildRoleUpdate -> {
                    val ctx = parseNode<GuildRoleEventContext>(node)
                    dataCache.cacheGuildRole(ctx.guildId, ctx.role)
                    guildRoleUpdateEventBlocks.forEach { it(ctx) }
                }
                GatewayEvent.GuildRoleDelete -> {
                    val ctx = parseNode<GuildRoleIdEventContext>(node)
                    dataCache.removeGuildRole(ctx.guildId, ctx.roleId)
                    guildRoleDeleteEventBlocks.forEach { it(ctx) }
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
                    val ctx = parseNode<MessageDeleteEventContext>(node)
                    dataCache.messageCache.synchronous().invalidate(
                        CombinedId(
                            ctx.channelId,
                            ctx.messageId
                        )
                    )
                    messageDeleteEventBlocks.forEach { it(ctx) }
                }
                GatewayEvent.MessageDeleteBulk -> {
                    val ctx = parseNode<MessageDeleteBulkEventContext>(node)
                    ctx.messageIds.forEach { msgId ->
                        dataCache.messageCache.synchronous().invalidate(
                            CombinedId(
                                ctx.channelId,
                                msgId
                            )
                        )
                    }
                    messageDeleteBulkEventBlocks.forEach { it(ctx) }
                }
                GatewayEvent.MessageReactionAdd -> {
                    val ctx = parseNode<MessageReactionAddEventContext>(node)
                    dataCache.messageReactionAdd(ctx)
                    messageReactionAddEventBlocks.forEach { it(ctx) }
                }
                GatewayEvent.MessageReactionRemove -> {
                    val ctx = parseNode<MessageReactionRemoveEventContext>(node)
                    dataCache.messageReactionRemove(ctx)
                    messageReactionRemoveEventBlocks.forEach { it(ctx) }
                }
                GatewayEvent.MessageReactionRemoveAll -> {
                    val ctx = parseNode<MessageReactionRemoveAllEventContext>(node)
                    dataCache.messageReactionRemoveAll(ctx)
                    messageReactionRemoveAllEventBlocks.forEach { it(ctx) }
                }
                GatewayEvent.PresenceUpdate -> {
                    // TODO
                }
                GatewayEvent.TypingStart -> {
                    typingStartEventBlocks.forEach { it(parseNode(node)) }
                }
                GatewayEvent.UserUpdate -> {
                    val user = parseNode<DiscordUser>(node)
                    client.botUser = user
                    userUpdateEventBlocks.forEach { it(user) }
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
