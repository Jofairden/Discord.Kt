package com.jofairden.discordkt.model.discord.message

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.JsonNode
import com.jofairden.discordkt.api.cache.getSuspending
import com.jofairden.discordkt.model.api.ApiAware
import com.jofairden.discordkt.model.discord.emoji.IEmoji
import com.jofairden.discordkt.model.discord.guild.GuildUser
import com.jofairden.discordkt.model.discord.message.embed.MessageEmbed
import com.jofairden.discordkt.model.discord.user.DiscordUser
import com.jofairden.discordkt.model.request.CreateMessageBody
import com.jofairden.discordkt.model.request.EditMessageBody
import com.jofairden.discordkt.util.lazyAsync
import java.util.Date

data class DiscordMessage(

    @JsonProperty("id")
    val id: Long,

    @JsonProperty("channel_id")
    val channelId: Long,

    @JsonProperty("guild_id")
    val guildId: Long?,

    @JsonProperty("author")
    val author: DiscordUser,

    @JsonProperty("member")
    val authorGuildUser: GuildUser?,

    @JsonProperty("content")
    val content: String?,

    @JsonProperty("timestamp")
    val timestamp: Date,

    @JsonProperty("edited_timestamp")
    val editedTimestamp: Date?,

    @JsonProperty("tts")
    val isTts: Boolean,

    @JsonProperty("mention_everyone")
    val mentionsEveryone: Boolean,

    @JsonProperty("mentions")
    val mentions: Array<DiscordUser>,

    @JsonProperty("mention_roles")
    val mentionedRoleIds: Array<Long>,

    @JsonProperty("mention_channels")
    val mentionedChannels: Array<JsonNode>?, // ChannelMention

    @JsonProperty("attachments")
    val attachments: Array<JsonNode>,

    @JsonProperty("embeds")
    val embeds: Array<JsonNode>,

    @JsonProperty("reactions")
    val reactions: Array<MessageReaction>?,

    @JsonProperty("nonce")
    val nonce: String?,

    @JsonProperty("pinned")
    val isPinned: Boolean,

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
    val flags: Int

) : ApiAware() {

    val channel by lazyAsync {
        dataCache.channelCache.getSuspending(channelId)
    }

    val guild by lazyAsync {
        channel.await().guild.await()
    }

    fun copyFromMessageUpdate(update: DiscordMessageUpdate) =
        this.copy(
            content = update.content,
            editedTimestamp = update.editedTimestamp ?: this.editedTimestamp,
            isTts = update.isTts ?: this.isTts,
            mentionsEveryone = update.mentionsEveryone ?: this.mentionsEveryone,
            mentions = update.mentions ?: this.mentions,
            mentionedRoleIds = update.mentionedRoleIds ?: this.mentionedRoleIds,
            mentionedChannels = update.mentionedChannels ?: this.mentionedChannels,
            attachments = update.attachments ?: this.attachments,
            embeds = update.embeds ?: this.embeds,
            reactions = update.reactions ?: this.reactions,
            nonce = update.nonce ?: this.nonce,
            isPinned = update.isPinned ?: this.isPinned,
            activity = update.activity ?: this.activity,
            application = update.application ?: this.application,
            referenceData = update.referenceData ?: this.referenceData,
            flags = update.flags ?: this.flags
        )

    suspend fun reply(text: String) =
        serviceProvider.channelService.postChannelMessage(
            channelId,
            CreateMessageBody(text)
        )

    suspend fun reply(embed: MessageEmbed) =
        serviceProvider.channelService.postChannelMessage(
            channelId,
            CreateMessageBody("", embed = embed)
        )

    suspend fun edit(body: EditMessageBody) =
        serviceProvider.channelService.editMessage(
            channelId,
            id,
            body
        )

    suspend fun react(reaction: IEmoji) =
        serviceProvider.channelService.createReaction(channelId, id, IEmoji.getText(reaction))
}
