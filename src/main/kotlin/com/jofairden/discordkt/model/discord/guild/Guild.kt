package com.jofairden.discordkt.model.discord.guild

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.JsonNode
import com.jofairden.discordkt.api.cache.getSuspending
import com.jofairden.discordkt.model.api.ApiAware
import com.jofairden.discordkt.model.discord.channel.DiscordChannel
import com.jofairden.discordkt.model.discord.emoji.DiscordEmoji
import com.jofairden.discordkt.model.discord.role.GuildRole
import com.jofairden.discordkt.model.discord.user.DiscordUserPresence
import com.jofairden.discordkt.model.request.CreateGuildChannelBody
import com.jofairden.discordkt.model.request.CreateGuildRoleBody
import com.jofairden.discordkt.model.request.ModifyGuildBody
import com.jofairden.discordkt.model.request.ModifyGuildMemberBody
import com.jofairden.discordkt.model.request.ModifyGuildRolePositionBody
import com.jofairden.discordkt.util.lazyAsync

data class Guild(

    @JsonProperty("id")
    val id: Long,

    @JsonProperty("name")
    val name: String,

    @JsonProperty("icon")
    val iconHash: String?,

    @JsonProperty("splash")
    val splashHash: String?,

    @JsonProperty("owner")
    val isCurrentUserOwner: Boolean?,

    @JsonProperty("owner_id")
    val ownerId: Long,

    @JsonProperty("permissions")
    val currentUserPermissions: Int?,

    @JsonProperty("region")
    val voiceRegion: String,

    @JsonProperty("afk_channel_id")
    val afkChannelId: Long?,

    @JsonProperty("afk_timeout")
    val afkTimeout: Int,

    @JsonProperty("embed_enabled")
    val embedEnabled: Boolean?,

    @JsonProperty("embed_channel_id")
    val embedChannelId: Long?,

    @JsonProperty("verification_level")
    val verificationLevel: GuildVerificationLevel,

    @JsonProperty("default_message_notifications")
    val defaultMessageNotifications: MessageNotificationLevel,

    @JsonProperty("explicit_content_filter")
    val explicitContentFilter: ExplicitContentFilterLevel,

    @JsonProperty("roles")
    val roles: Array<GuildRole>, // Role

    @JsonProperty("emojis")
    val emojis: Array<DiscordEmoji>, // Emoji

    @JsonProperty("features")
    val features: Array<GuildFeature>, // Feature

    @JsonProperty("mfa_level")
    val mfaLevel: MfaLevel,

    @JsonProperty("application_id")
    val applicationId: Long?,

    @JsonProperty("widget_enabled")
    val widgetEnabled: Boolean?,

    @JsonProperty("widget_channel_id")
    val widgetChannelId: Long?,

    @JsonProperty("system_channel_id")
    val systemChannelId: Long?,

    ///
    // GUILD_CREATE START
    ///

    @JsonProperty("joined_at")
    val joinedAt: String?,

    @JsonProperty("large")
    val isLarge: Boolean?,

    @JsonProperty("unavailable")
    val isUnavailable: Boolean?,

    @JsonProperty("voice_states")
    val voiceStates: Array<JsonNode>?,

    @JsonProperty("members")
    val members: Array<GuildUser>?, // user

    @JsonProperty("channels")
    val channels: Array<DiscordChannel>?, // Channel

    @JsonProperty("presences")
    val presences: Array<DiscordUserPresence>?, // Partial presence update

    ///
    // GUILD_CREATE END
    ///

    @JsonProperty("max_presences")
    val maxPresenceCount: Int?,

    @JsonProperty("max_members")
    val maxMemberCount: Int?,

    @JsonProperty("vanity_url_code")
    val vanityUrlCode: String?,

    @JsonProperty("description")
    val description: String?,

    @JsonProperty("banner")
    val bannerHash: String?,

    @JsonProperty("premium_tier")
    val premiumTier: GuildPremiumTier,

    @JsonProperty("premium_subscription_count")
    val boostingCount: Int?,

    @JsonProperty("preferred_locale")
    val preferredLocale: String,

    // Undocumented
    @JsonProperty("discovery_splash")
    val discoverySplashHash: String?,

    @JsonProperty("system_channel_flags")
    val systemChannelFlags: Int,

    @JsonProperty("rules_channel_id")
    val rulesChannelId: Long?
) : ApiAware() {

    val owner by lazyAsync {
        dataCache.userCache.getSuspending(ownerId)
    }

    val memberUsers
        get() = members?.mapNotNull { it.discordUser } ?: listOf()

    suspend fun get() = serviceProvider.guildService.getGuild(id)
    suspend fun modify(body: ModifyGuildBody = ModifyGuildBody()) = serviceProvider.guildService.modifyGuild(id, body)
    suspend fun getChannels() = serviceProvider.guildService.getGuildChannels(id)
    suspend fun createChannel(body: CreateGuildChannelBody) = serviceProvider.guildService.createGuildChannel(id, body)
    suspend fun getMembers(limit: Int = 50, after: Long? = null) =
        serviceProvider.guildService.getGuildMembers(id, limit, after)

    suspend fun getMember(userId: Long) = serviceProvider.guildService.getGuildMember(id, userId)
    suspend fun modifyMember(userId: Long, body: ModifyGuildMemberBody) =
        serviceProvider.guildService.modifyGuildMember(id, userId, body)

    suspend fun modifyCurrentUserNick(nick: String) = serviceProvider.guildService.modifyCurrentUserNick(id, nick)
    suspend fun addGuildMemberRole(userId: Long, roleId: Long) =
        serviceProvider.guildService.addGuildMemberRole(id, userId, roleId)

    suspend fun removeMemberRole(userId: Long, roleId: Long) =
        serviceProvider.guildService.removeGuildMemberRole(id, userId, roleId)

    suspend fun removeMember(userId: Long) = serviceProvider.guildService.removeGuildMember(id, userId)
    suspend fun getBans() = serviceProvider.guildService.getGuildBans(id)
    suspend fun getUserBans(userId: Long) = serviceProvider.guildService.getGuildBan(id, userId)
    suspend fun banUser(userId: Long, deleteMessageDays: Int? = null, reason: String? = null) =
        serviceProvider.guildService.createGuildBan(id, userId, deleteMessageDays, reason)

    suspend fun unbanUser(userId: Long) = serviceProvider.guildService.removeGuildBan(id, userId)
    suspend fun getRoles() = serviceProvider.guildService.getGuildRoles(id)
    suspend fun createRole(body: CreateGuildRoleBody = CreateGuildRoleBody()) =
        serviceProvider.guildService.createGuildRole(id, body)

    suspend fun modifyRolePositions(body: Array<ModifyGuildRolePositionBody>) =
        serviceProvider.guildService.modifyGuildRolePositions(id, body)

    suspend fun modifyRole(roleId: Long, body: CreateGuildRoleBody) =
        serviceProvider.guildService.modifyGuildRole(id, roleId, body)

    suspend fun getPruneCount(days: Int = 7) = serviceProvider.guildService.getGuildPruneCount(id, days)
    suspend fun pruneMembers(days: Int, computePruneCount: Boolean = true) =
        serviceProvider.guildService.beginGuildPrune(id, days, computePruneCount)

    suspend fun getInvites() = serviceProvider.guildService.getGuildInvites(id)
    suspend fun getEmbed() = serviceProvider.guildService.getGuildEmbed(id)
    suspend fun modifyEmbed(embed: GuildEmbed) = serviceProvider.guildService.modifyGuildEmbed(id, embed)
}
