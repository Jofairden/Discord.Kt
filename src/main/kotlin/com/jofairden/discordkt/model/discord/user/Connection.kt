package com.jofairden.discordkt.model.discord.user

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.JsonNode

data class Connection(

    @JsonProperty("id")
    val id: String,

    @JsonProperty("name")
    val name: String,

    @JsonProperty("type")
    val type: String,

    @JsonProperty("revoked")
    val isRevoked: Boolean,

    @JsonProperty("integrations")
    val integrations: Array<JsonNode>,

    @JsonProperty("verified")
    val isVerified: Boolean,

    @JsonProperty("friend_sync")
    val isFriendSyncEnabled: Boolean,

    @JsonProperty("show_activity")
    val isShownInPresenceUpdates: Boolean,

    @JsonProperty("visibility")
    val visibility: Int

)
