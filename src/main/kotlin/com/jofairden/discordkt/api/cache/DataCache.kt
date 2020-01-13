package com.jofairden.discordkt.api.cache

import com.github.benmanes.caffeine.cache.AsyncLoadingCache
import com.github.benmanes.caffeine.cache.Caffeine
import com.jofairden.discordkt.api.ApiServiceProvider
import com.jofairden.discordkt.model.api.DataCacheProperties
import com.jofairden.discordkt.model.discord.channel.DiscordChannel
import com.jofairden.discordkt.model.discord.emoji.DiscordEmoji
import com.jofairden.discordkt.model.discord.guild.Guild
import com.jofairden.discordkt.model.discord.guild.GuildUser
import com.jofairden.discordkt.model.discord.message.DiscordMessage
import com.jofairden.discordkt.model.discord.role.GuildRole
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.future.await
import kotlinx.coroutines.withContext
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
        .buildAsync<Long, Array<GuildUser>>(ApiAsyncLoader { key -> serviceProvider.guildService.getGuildMembers(key) })

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
        users.forEach { user -> user.update(roles) }
    }

    fun GuildUser.update(roles: Array<GuildRole>) {
        val matching = roles.asSequence().filter { role -> this.roleIds.contains(role.id) }
        this.roles = matching.toList()
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
            cacheChannel(it)
        }

        updateGuildUsers(guild.id)
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
}
