package com.jofairden.discordkt.model.request

import com.fasterxml.jackson.annotation.JsonProperty

data class ModifyGuildMemberBody(
    @JsonProperty("nick")
    val nick: String? = null,
    @JsonProperty("roles")
    val roleIds: Array<Long>? = null,
    @JsonProperty("mute")
    val isMuted: Boolean? = null,
    @JsonProperty("deaf")
    val isDeaf: Boolean? = null,
    @JsonProperty("channel_id")
    val channelId: Long? = null
)
