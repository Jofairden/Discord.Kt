package com.jofairden.discordkt.model.discord.message

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.JsonNode
import com.jofairden.discordkt.model.discord.guild.GuildUser
import com.jofairden.discordkt.model.discord.user.DiscordUser
import java.util.Date

data class DiscordMessageUpdate(
    @JsonProperty("id")
    val id: Long,

    @JsonProperty("channel_id")
    val channelId: Long,

    @JsonProperty("guild_id")
    val guildId: Long?,

    @JsonProperty("author")
    val author: DiscordUser?,

    @JsonProperty("member")
    val authorGuildUser: GuildUser?,

    @JsonProperty("content")
    val content: String?,

    @JsonProperty("timestamp")
    val timestamp: Date?,

    @JsonProperty("edited_timestamp")
    val editedTimestamp: Date?,

    @JsonProperty("tts")
    val isTts: Boolean?,

    @JsonProperty("mention_everyone")
    val mentionsEveryone: Boolean?,

    @JsonProperty("mentions")
    val mentions: Array<DiscordUser>?,

    @JsonProperty("mention_roles")
    val mentionedRoleIds: Array<Long>?,

    @JsonProperty("mention_channels")
    val mentionedChannels: Array<JsonNode>?, // ChannelMention

    @JsonProperty("attachments")
    val attachments: Array<JsonNode>?,

    @JsonProperty("embeds")
    val embeds: Array<JsonNode>?,

    @JsonProperty("reactions")
    val reactions: Array<MessageReaction>?,

    @JsonProperty("nonce")
    val nonce: String?,

    @JsonProperty("pinned")
    val isPinned: Boolean?,

    @JsonProperty("webhook_id")
    val webhookId: Long?,

    @JsonProperty("type")
    val messageType: MessageType?,

    @JsonProperty("activity")
    val activity: JsonNode?,

    @JsonProperty("application")
    val application: JsonNode?,

    @JsonProperty("message_reference")
    val referenceData: JsonNode?,

    @JsonProperty("flags")
    val flags: Int?
)
