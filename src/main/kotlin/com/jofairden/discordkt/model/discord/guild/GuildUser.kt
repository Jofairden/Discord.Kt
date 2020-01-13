package com.jofairden.discordkt.model.discord.guild

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.jofairden.discordkt.model.discord.role.GuildRole
import com.jofairden.discordkt.model.discord.user.DiscordUser
import java.util.Date

@JsonIgnoreProperties(ignoreUnknown = true)
data class GuildUser(
    @JsonProperty("user")
    val discordUser: DiscordUser?,

    @JsonProperty("nick")
    val nickname: String?,

    @JsonProperty("roles")
    private val _roleIds: Array<Long>?,

    @JsonProperty("joined_at")
    val joinedAt: Date,

    @JsonProperty("premium_since")
    val boostedSince: String?,

    @JsonProperty("deaf")
    val isDeafenedInVoice: Boolean,

    @JsonProperty("muted")
    val isMutedInVoice: Boolean
) {
    @JsonIgnore
    var roles = listOf<GuildRole>()
        internal set

    @JsonIgnore
    val roleIds: Array<Long> = _roleIds ?: arrayOf()

    // fun setRoleIds(roleIds: Array<Long>?) {
    //
    // }

    // @JsonProperty("roles")
    // var roleIds: Array<Long>
}
