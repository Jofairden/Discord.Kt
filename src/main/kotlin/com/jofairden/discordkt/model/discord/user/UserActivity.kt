package com.jofairden.discordkt.model.discord.user

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonValue
import com.jofairden.discordkt.model.discord.emoji.DiscordEmoji
import java.util.Date

data class UserStatusActivity(
    @JsonProperty("name")
    val name: String,

    @JsonProperty("type")
    val type: ActivityType,

    @JsonProperty("url")
    val streamUrl: String? = null,

    @JsonProperty("timestamps")
    val timestamps: Timestamps? = null,

    @JsonProperty("application_id")
    val applicationId: String? = null,

    @JsonProperty("details")
    val details: String? = null,

    @JsonProperty("state")
    val partyStatus: String? = null,

    @JsonProperty("party")
    val party: ActivityParty? = null,

    @JsonProperty("assets")
    val assets: Assets? = null,

    @JsonProperty("secrets")
    val secrets: RichPresenceSecrets? = null,

    @JsonProperty("instance")
    val activityIsInstanced: Boolean? = null,

    @JsonProperty("flags")
    val activityFlags: Int? = null,

    @JsonProperty("emoji")
    val emoji: DiscordEmoji? = null
)

data class Timestamps(
    @JsonProperty("start")
    val start: Date? = null,

    @JsonProperty("end")
    val end: Date? = null
)

data class ActivityParty(
    @JsonProperty("id")
    val id: String? = null,

    @JsonProperty("size")
    val size: List<Int>? = null
)

data class Assets(
    @JsonProperty("large_image")
    val largeImage: String? = null,

    @JsonProperty("large_text")
    val largeImageText: String? = null,

    @JsonProperty("small_image")
    val smallImage: String? = null,

    @JsonProperty("small_text")
    val smallImageText: String? = null
)

data class RichPresenceSecrets(
    @JsonProperty("join")
    val joinParty: String? = null,

    @JsonProperty("spectate")
    val spectate: String? = null,

    @JsonProperty("match")
    val joinInstance: String? = null
)

enum class ActivityType(
    @JsonValue
    val code: Int
) {
    Game(0),
    Steaming(1),
    Listening(2),
    Unknown(3),
    Custom(4)
}
