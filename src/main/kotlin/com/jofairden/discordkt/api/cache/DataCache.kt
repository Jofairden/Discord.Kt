package com.jofairden.discordkt.api.cache

import com.github.benmanes.caffeine.cache.AsyncCache
import com.github.benmanes.caffeine.cache.Caffeine
import com.jofairden.discordkt.api.ApiServiceProvider
import com.jofairden.discordkt.model.api.DataCacheProperties
import com.jofairden.discordkt.model.discord.emoji.DiscordEmoji
import com.jofairden.discordkt.model.discord.guild.Guild
import com.jofairden.discordkt.model.discord.guild.GuildUser
import com.jofairden.discordkt.model.discord.message.DiscordMessage
import com.jofairden.discordkt.model.discord.role.GuildRole
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.future.asCompletableFuture
import kotlinx.coroutines.future.await
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit
import kotlin.coroutines.coroutineContext

internal class DataCache(
    dataCacheProperties: DataCacheProperties,
    private val serviceProvider: ApiServiceProvider
) {
    val guilds = Caffeine.newBuilder()
        .expireAfterAccess(5, TimeUnit.MINUTES)
        .maximumSize(dataCacheProperties.guildCacheMaxSize)
        .buildAsync<Long, Guild>()

    val guildUsers = Caffeine.newBuilder()
        .expireAfterAccess(5, TimeUnit.MINUTES)
        .maximumSize(dataCacheProperties.guildUsersCacheMaxSize)
        .buildAsync<Long, Array<GuildUser>>()

    val guildRoles = Caffeine.newBuilder()
        .expireAfterAccess(5, TimeUnit.MINUTES)
        .maximumSize(dataCacheProperties.guildRolesCacheMaxSize)
        .buildAsync<Long, Array<GuildRole>>()

    val guildEmojiCache = Caffeine.newBuilder()
        .expireAfterAccess(5, TimeUnit.MINUTES)
        .maximumSize(dataCacheProperties.emojiCacheMaxSize)
        .buildAsync<Long, DiscordEmoji>()

    val messageCache = Caffeine.newBuilder()
        .expireAfterAccess(5, TimeUnit.MINUTES)
        .maximumSize(dataCacheProperties.messageCacheMaxSize)
        .buildAsync<Long, DiscordMessage>()

    suspend fun getMessage(channelId: Long, messageId: Long) =
        messageCache.getSuspending<Long, DiscordMessage>(messageId) { _ ->
            serviceProvider.channelService.getChannelMessage(channelId, messageId).also {
                cacheMessage(it)
            }
        }

    suspend fun getGuildUsers(guildId: Long) =
        guildUsers.getSuspending<Long, Array<GuildUser>>(guildId) { id ->
            serviceProvider.guildService.getGuildMembers(id).toTypedArray().also {
                cacheGuildUsers(id, it)
            }
        }

    suspend fun getGuildRoles(guildId: Long) =
        guildRoles.getSuspending<Long, Array<GuildRole>>(guildId) { id ->
            serviceProvider.guildService.getGuildRoles(id).toTypedArray().also {
                cacheGuildRoles(id, it)
            }
        }

    suspend fun enrich(message: DiscordMessage): DiscordMessage {
        var enrichedMessage = message
        if (message.guildId != null) {
            val users = getGuildUsers(message.guildId)
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
        val users = getGuildUsers(guildId)
        val roles = getGuildRoles(guildId)
        users.forEach { user -> user.update(roles) }
    }

    fun GuildUser.update(roles: Array<GuildRole>) {
        val matching = roles.asSequence().filter { role -> this.roleIds.contains(role.id) }
        this.roles = matching.toList()
    }

    suspend fun cacheGuild(guild: Guild) {
        guilds.put(guild.id, CompletableFuture.completedFuture(guild))
        guild.members?.let {
            cacheGuildUsers(guild.id, it)
        }
        cacheGuildRoles(guild.id, guild.roles)
        guild.emojis.filter { it.id != null }.forEach {
            cacheEmoji(it)
        }

        updateGuildUsers(guild.id)
    }

    fun cacheGuildUsers(guildId: Long, members: Array<GuildUser>) {
        guildUsers.put(guildId, CompletableFuture.completedFuture(members))
    }

    fun cacheGuildRoles(guildId: Long, roles: Array<GuildRole>) {
        guildRoles.put(guildId, CompletableFuture.completedFuture(roles))
    }

    fun cacheEmoji(emoji: DiscordEmoji) {
        guildEmojiCache.put(emoji.id!!, CompletableFuture.completedFuture(emoji))
    }

    fun cacheMessage(message: DiscordMessage) {
        messageCache.put(message.id, CompletableFuture.completedFuture(message))
    }

    /**
     * Helper function to get from cache in suspending fashion using a suspending call fallback
     */
    suspend fun <K : Any, V> AsyncCache<K, V>.getSuspending(key: K, mapper: suspend (key: K) -> V): V {
        val outerContext = coroutineContext // Get the current context
        /**
         *  The context used to call loadValue is composed of three things:
         *
         *  * The original context that was used to call getSuspending
         *  * A new Job(), which overrides the job in the outer context. This is necessary to ensure that errors raised in loadValue don't propagate immediately but are instead captured in the CompletableFuture so that the cache can respond to them appropriately.
         *  * The executor, so that the configuration of the cache with regard to threads is still respected.
         *
         *  This does mean that the context is sensitive to where the get was called from, which might not be the right behaviour if the cache is shared by different parts of the application.
         */
        return get(key) { k: K, executor: Executor ->
            val innerContext = outerContext + Job() + executor.asCoroutineDispatcher()
            CoroutineScope(innerContext).async {
                mapper(k)
            }.asCompletableFuture()
        }.await()
    }
}
