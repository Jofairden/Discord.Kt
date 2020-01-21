package com.jofairden.discordkt.model.request

import com.fasterxml.jackson.annotation.JsonProperty
import com.jofairden.discordkt.model.discord.guild.ExplicitContentFilterLevel
import com.jofairden.discordkt.model.discord.guild.GuildVerificationLevel
import com.jofairden.discordkt.model.discord.guild.MessageNotificationLevel

data class ModifyGuildBody(
    @JsonProperty("name")
    val name: String? = null,
    @JsonProperty("region")
    val region: String? = null,
    @JsonProperty("verification_level")
    val verificationLevel: GuildVerificationLevel? = null,
    @JsonProperty("default_message_notifications")
    val defaultMessageNotificationLevel: MessageNotificationLevel? = null,
    @JsonProperty("explicit_content_filter")
    val explicitContentFilterLevel: ExplicitContentFilterLevel? = null,
    @JsonProperty("afk_channel_id")
    val afkChannelId: Long? = null,
    @JsonProperty("afk_timeout")
    val afkTimeoutInSeconds: Int? = null,
    @JsonProperty("icon")
    val iconHash: String? = null,
    @JsonProperty("splash")
    val splashHash: String? = null,
    @JsonProperty("banner")
    val bannerHash: String? = null,
    @JsonProperty("system_channel_id")
    val systemChannelId: Long? = null
)

