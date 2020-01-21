package com.jofairden.discordkt.model.request

import com.fasterxml.jackson.annotation.JsonProperty
import com.jofairden.discordkt.model.discord.channel.Overwrite

data class ModifyChannelBody(
    @JsonProperty("name")
    val name: String? = null,
    @JsonProperty("position")
    val position: Int? = null,
    @JsonProperty("topic")
    val topic: String? = null,
    @JsonProperty("nsfw")
    val nsfw: Boolean? = null,
    @JsonProperty("rate_limit_per_user")
    val rateLimitPerUser: Int? = null,
    @JsonProperty("bitrate")
    val bitrate: Int? = null,
    @JsonProperty("user_limit")
    val userVoiceLimit: Int? = null,
    @JsonProperty("permission_overwrites")
    val permissionOverwrites: Array<Overwrite>? = null,
    @JsonProperty("parent_id")
    val parentId: Long? = null
)
