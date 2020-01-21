package com.jofairden.discordkt.model.discord.guild

import com.fasterxml.jackson.annotation.JsonProperty
import com.jofairden.discordkt.model.discord.channel.DiscordChannel
import com.jofairden.discordkt.model.discord.user.DiscordUser
import java.util.Date

data class GuildInvite(
    @JsonProperty("code")
    val code: String,
    @JsonProperty("guild")
    val guild: PartialGuild?,
    @JsonProperty("channel")
    val channel: DiscordChannel,
    @JsonProperty("target_user")
    val targetUser: DiscordUser?,
    @JsonProperty("target_user_type")
    val targetUserType: Int?,
    @JsonProperty("approximate_presence_count")
    val approximatePresenceCount: Int?,
    @JsonProperty("approximate_member_count")
    val approximateMemberCount: Int?,
    @JsonProperty("inviter")
    val inviter: DiscordUser?,
    @JsonProperty("uses")
    val uses: Int,
    @JsonProperty("max_uses")
    val maxUses: Int,
    @JsonProperty("max_age")
    val expiryInSeconds: Int,
    @JsonProperty("temporary")
    val isTemporary: Boolean,
    @JsonProperty("created_at")
    val creationDate: Date
) {
    val url = "https://www.discord.gg/${code}"
}
