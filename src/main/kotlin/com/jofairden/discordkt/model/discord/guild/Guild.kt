package com.jofairden.discordkt.model.discord.guild

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.JsonNode
import com.jofairden.discordkt.model.discord.emoji.DiscordEmoji
import com.jofairden.discordkt.model.discord.role.GuildRole
import com.jofairden.discordkt.model.discord.user.DiscordUserPresence

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
    val channels: Array<JsonNode>?, // Channel

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
)
