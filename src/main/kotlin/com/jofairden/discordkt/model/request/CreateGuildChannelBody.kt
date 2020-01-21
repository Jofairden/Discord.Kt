package com.jofairden.discordkt.model.request

import com.fasterxml.jackson.annotation.JsonProperty
import com.jofairden.discordkt.model.discord.channel.ChannelType
import com.jofairden.discordkt.model.discord.channel.Overwrite

data class CreateGuildChannelBody(
    @JsonProperty("name")
    val name: String,
    @JsonProperty("type")
    val type: ChannelType? = null,
    @JsonProperty("topic")
    val topic: String? = null,
    @JsonProperty("bitrate")
    val bitrate: Int? = null,
    @JsonProperty("user_limit")
    val voiceUserLimit: Int? = null,
    @JsonProperty("rate_limit_per_user")
    val rateLimitPerUser: Int? = null,
    @JsonProperty("position")
    val position: Int? = null,
    @JsonProperty("permission_overwrites")
    val permissionOverwrites: Array<Overwrite>? = null,
    @JsonProperty("parent_id")
    val parentId: Long? = null,
    @JsonProperty("nsfw")
    val nsfw: Boolean? = null
)
