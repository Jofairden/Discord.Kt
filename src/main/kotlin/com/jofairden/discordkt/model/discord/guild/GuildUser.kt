package com.jofairden.discordkt.model.discord.guild

import com.fasterxml.jackson.annotation.JsonProperty
import com.jofairden.discordkt.api.DiscordClient
import com.jofairden.discordkt.model.discord.user.DiscordUser
import java.util.Date

data class GuildUser(
    @JsonProperty("user")
    val discordUser: DiscordUser?,

    @JsonProperty("nick")
    val nickname: String?,

    @JsonProperty("roles")
    val roleIds: Array<Long>,

    @JsonProperty("joined_at")
    val joinedAt: Date,

    @JsonProperty("premium_since")
    val boostedSince: String?,

    @JsonProperty("deaf")
    val isDeafenedInVoice: Boolean,

    @JsonProperty("muted")
    val isMutedInVoice: Boolean
) {
    // TODO this is shitty
    suspend fun DiscordClient.getRoles(id: Long) =
        serviceProvider.guildService.getGuildRoles(id).filter { roleIds.contains(it.id) }
}
