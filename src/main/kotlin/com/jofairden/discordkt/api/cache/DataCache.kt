package com.jofairden.discordkt.api.cache

import com.github.benmanes.caffeine.cache.AsyncLoadingCache
import com.github.benmanes.caffeine.cache.Caffeine
import com.jofairden.discordkt.api.ApiServiceProvider
import com.jofairden.discordkt.model.api.DataCacheProperties
import com.jofairden.discordkt.model.context.event.GuildMemberUpdateEventContext
import com.jofairden.discordkt.model.context.event.MessageReactionAddEventContext
import com.jofairden.discordkt.model.context.event.MessageReactionRemoveAllEventContext
import com.jofairden.discordkt.model.context.event.MessageReactionRemoveEventContext
import com.jofairden.discordkt.model.discord.channel.DiscordChannel
import com.jofairden.discordkt.model.discord.emoji.DiscordEmoji
import com.jofairden.discordkt.model.discord.emoji.UnicodeEmoji
import com.jofairden.discordkt.model.discord.guild.Guild
import com.jofairden.discordkt.model.discord.guild.GuildUser
import com.jofairden.discordkt.model.discord.message.DiscordMessage
import com.jofairden.discordkt.model.discord.message.MessageReaction
import com.jofairden.discordkt.model.discord.role.GuildRole
import com.jofairden.discordkt.model.discord.user.DiscordUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.future.await
import kotlinx.coroutines.withContext
import mu.KotlinLogging
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit
import kotlin.coroutines.coroutineContext

typealias CombinedId = Pair<Long, Long>

/**
 * Helper function to get from cache in suspending fashion using a suspending call fallback
 */
suspend fun <K : Any, V> AsyncLoadingCache<K, V>.getSuspending(key: K): V {
    val outerContext = coroutineContext // Get the current context
    val innerContext = outerContext + Job()
    return withContext(CoroutineScope(innerContext).coroutineContext) {
        get(key).await()
    }
}

class DataCache(
    dataCacheProperties: DataCacheProperties,
    private val serviceProvider: ApiServiceProvider
) {

    private val logger = KotlinLogging.logger {}

    val guildCache = Caffeine.newBuilder()
        .expireAfterAccess(5, TimeUnit.MINUTES)
        .maximumSize(dataCacheProperties.guildCacheMaxSize)
        .buildAsync<Long, Guild>(ApiAsyncLoader { key ->
            serviceProvider.guildService.getGuild(key).also {
                cacheGuild(it)
            }
        })

    val guildUserCache = Caffeine.newBuilder()
        .expireAfterAccess(5, TimeUnit.MINUTES)
        .maximumSize(dataCacheProperties.guildUsersCacheMaxSize)
        .buildAsync<Long, Array<GuildUser>>(ApiAsyncLoader { key ->
            val roles = guildRoleCache.getSuspending(key)
            serviceProvider.guildService.getGuildMembers(key).map {
                it.update(roles)
            }.toTypedArray()
        })

    val userCache = Caffeine.newBuilder()
        .expireAfterAccess(5, TimeUnit.MINUTES)
        .maximumSize(dataCacheProperties.userCacheMaxSize)
        .buildAsync<Long, DiscordUser>(ApiAsyncLoader { key ->
            serviceProvider.userService.getUser(key)
        })

    val guildRoleCache = Caffeine.newBuilder()
        .expireAfterAccess(5, TimeUnit.MINUTES)
        .maximumSize(dataCacheProperties.guildRolesCacheMaxSize)
        .buildAsync<Long, Array<GuildRole>>(ApiAsyncLoader { key -> serviceProvider.guildService.getGuildRoles(key) })

    val guildEmojiCache = Caffeine.newBuilder()
        .expireAfterAccess(5, TimeUnit.MINUTES)
        .maximumSize(dataCacheProperties.emojiCacheMaxSize)
        .buildAsync<CombinedId, DiscordEmoji>(ApiAsyncLoader { keys ->
            serviceProvider.emojiService.getGuildEmoji(
                keys.first,
                keys.second
            )
        })

    val channelCache = Caffeine.newBuilder()
        .expireAfterAccess(5, TimeUnit.MINUTES)
        .maximumSize(dataCacheProperties.channelCacheMaxSize)
        .buildAsync<Long, DiscordChannel>(ApiAsyncLoader { key ->
            serviceProvider.channelService.getChannel(key)
        })

    val messageCache = Caffeine.newBuilder()
        .expireAfterAccess(5, TimeUnit.MINUTES)
        .maximumSize(dataCacheProperties.messageCacheMaxSize)
        .buildAsync<CombinedId, DiscordMessage>(ApiAsyncLoader { keys ->
            serviceProvider.channelService.getChannelMessage(
                keys.first,
                keys.second
            )
        })

    suspend fun enrich(message: DiscordMessage): DiscordMessage {
        var enrichedMessage = message
        if (message.guildId != null) {
            val users = guildUserCache.getSuspending(message.guildId)

            val user = users.firstOrNull { it.discordUser?.id == message.author.id }
            enrichedMessage = if (user?.discordUser != null) {
                enrichedMessage.copy(author = user.discordUser, authorGuildUser = user)
            } else {
                enrichedMessage.copy(authorGuildUser = user)
            }
            val mentionedUsers = users.filter { u -> enrichedMessage.mentions.any { it.id == u.discordUser?.id } }
                .map { it.discordUser!! }
            enrichedMessage = enrichedMessage.copy(mentions = mentionedUsers.toTypedArray())
        }
        return enrichedMessage
    }

    suspend fun updateGuildUsers(guildId: Long) {
        val users = guildUserCache.getSuspending(guildId)
        val roles = guildRoleCache.getSuspending(guildId)
        val newUsers = users.map { user -> user.update(roles) }
        cacheGuildUsers(guildId, newUsers.toTypedArray())
    }

    fun GuildUser.update(roles: Array<GuildRole>): GuildUser {
        val matching = roles.asSequence().filter { role -> this.roleIds.contains(role.id) }
        return this.copy(
            _roleIds = matching.map { it.id }.toList().toTypedArray()
        ).apply {
            this.roles = matching.toList().toTypedArray()
        }
    }

    suspend fun cacheGuild(guild: Guild) {
        guild.members?.let {
            cacheGuildUsers(guild.id, it)
        }
        cacheGuildRoles(guild.id, guild.roles)
        guild.emojis.filter { it.id != null }.forEach {
            cacheEmoji(guild.id, it)
        }
        guild.channels?.forEach {
            cacheChannel(it.copy(guildId = guild.id))
        }
        updateGuildUsers(guild.id)
    }

    suspend fun cacheGuildUser(guildId: Long, guildUser: GuildUser) {
        val users = guildUserCache.getSuspending(guildId).toMutableList()
        val index = users.indexOfFirst { it.discordUser?.id == guildUser.discordUser?.id }
        if (index != -1) {
            users.removeAt(index)
            users.add(guildUser)
            guildUserCache.put(guildId, CompletableFuture.completedFuture(users.toTypedArray()))
        }
    }

    fun cacheGuildUsers(guildId: Long, members: Array<GuildUser>) {
        guildUserCache.put(guildId, CompletableFuture.completedFuture(members))
    }

    fun cacheGuildRoles(guildId: Long, roles: Array<GuildRole>) {
        guildRoleCache.put(guildId, CompletableFuture.completedFuture(roles))
    }

    fun cacheEmoji(guildId: Long, emoji: DiscordEmoji) {
        guildEmojiCache.put(CombinedId(guildId, emoji.id!!), CompletableFuture.completedFuture(emoji))
    }

    fun cacheChannel(channel: DiscordChannel) {
        channelCache.put(channel.id, CompletableFuture.completedFuture(channel))
    }

    fun cacheMessage(message: DiscordMessage) {
        messageCache.put(CombinedId(message.channelId, message.id), CompletableFuture.completedFuture(message))
    }

    fun cacheUser(user: DiscordUser) {
        userCache.put(user.id, CompletableFuture.completedFuture(user))
    }

    suspend fun cacheGuildRole(guildId: Long, guildRole: GuildRole) {
        val roles = guildRoleCache.getSuspending(guildId)
        if (!roles.contains(guildRole)) {
            guildRoleCache.put(
                guildId, CompletableFuture.completedFuture(
                    roles.toMutableList().apply {
                        add(guildRole)
                    }.toTypedArray()
                )
            )
        }
    }

    suspend fun removeGuildRole(guildId: Long, guildRoleId: Long) {
        val roles = guildRoleCache.getSuspending(guildId)
        val role = roles.find { it.id == guildRoleId }
        if (role != null) {
            cacheGuildRoles(guildId, roles.toMutableList().filter {
                it.id != guildRoleId
            }.toTypedArray())
        }
        updateGuildUsers(guildId)
    }

    suspend fun guildMemberUpdate(ctx: GuildMemberUpdateEventContext) {
        cacheUser(ctx.user)
        val users = guildUserCache.getSuspending(ctx.guildId)
        val roles = guildRoleCache.getSuspending(ctx.guildId)
        users.find { it.discordUser?.id == ctx.user.id }?.let { user ->
            val matchingRoles = ctx.roleIds.mapNotNull { roleId -> roles.find { it.id == roleId } }.toTypedArray()
            val newUser = user.copy(_roleIds = matchingRoles.map { it.id }.toTypedArray()).update(roles)
            cacheGuildUser(ctx.guildId, newUser)
        }
    }

    suspend fun messageReactionAdd(ctx: MessageReactionAddEventContext) {
        val combinedId = CombinedId(ctx.channelId, ctx.messageId)
        if (!messageCache.asMap().containsKey(combinedId)) return

        val msg = messageCache.getSuspending(combinedId)
        val emoji = if (ctx.emoji.id == null) UnicodeEmoji(id = ctx.emoji.id, name = ctx.emoji.name)
        else ctx.emoji

        val reaction = if (emoji.id == null) msg.reactions?.find { it.emoji.name == emoji.name }
        else msg.reactions?.find { it.emoji.id == emoji.id }

        if (reaction != null) {
            val reactions = msg.reactions!!.toMutableList()
            reactions.remove(reaction)
            reactions.add(
                MessageReaction(
                    reaction.count + 1,
                    reaction.me,
                    reaction.emoji
                )
            )
            cacheMessage(
                msg.copy(reactions = reactions.toTypedArray())
            )
        } else {
            cacheMessage(
                msg.copy(
                    reactions = (msg.reactions ?: arrayOf()).toMutableList().apply {
                        add(
                            MessageReaction(
                                count = 1,
                                me = false,// TODO
                                emoji = emoji
                            )
                        )
                    }.toTypedArray()
                )
            )
        }
    }

    suspend fun messageReactionRemove(ctx: MessageReactionRemoveEventContext) {
        val combinedId = CombinedId(ctx.channelId, ctx.messageId)
        if (!messageCache.asMap().containsKey(combinedId)) return

        val msg = messageCache.getSuspending(combinedId)
        val emoji = if (ctx.emoji.id == null) UnicodeEmoji(id = ctx.emoji.id, name = ctx.emoji.name)
        else ctx.emoji

        val reaction = if (emoji.id == null) msg.reactions?.find { it.emoji.name == emoji.name }
        else msg.reactions?.find { it.emoji.id == emoji.id }

        if (reaction != null) {
            val reactions = msg.reactions!!.toMutableList()
            reactions.remove(reaction)
            val newCount = reaction.count - 1
            if (newCount > 0) {
                reactions.add(
                    MessageReaction(
                        newCount,
                        reaction.me,
                        reaction.emoji
                    )
                )
            }
            cacheMessage(
                msg.copy(reactions = reactions.toTypedArray())
            )
        } else {
            logger.warn { "messageReactionRemove could not find reaction object even though reaction was removed" }
        }
    }

    suspend fun messageReactionRemoveAll(ctx: MessageReactionRemoveAllEventContext) {
        val combinedId = CombinedId(ctx.channelId, ctx.messageId)
        if (!messageCache.asMap().containsKey(combinedId)) return

        val msg = messageCache.getSuspending(combinedId)
        cacheMessage(
            msg.copy(
                reactions = arrayOf()
            )
        )
    }
}
