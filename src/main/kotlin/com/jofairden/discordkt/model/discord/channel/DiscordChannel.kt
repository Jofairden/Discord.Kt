package com.jofairden.discordkt.model.discord.channel

import com.fasterxml.jackson.annotation.JsonProperty
import com.jofairden.discordkt.api.cache.getSuspending
import com.jofairden.discordkt.model.api.ApiAware
import com.jofairden.discordkt.model.discord.emoji.IEmoji
import com.jofairden.discordkt.model.discord.message.embed.MessageEmbed
import com.jofairden.discordkt.model.discord.user.DiscordUser
import com.jofairden.discordkt.model.request.CreateChannelInviteBody
import com.jofairden.discordkt.model.request.CreateMessageBody
import com.jofairden.discordkt.model.request.EditChannelPermissionsBody
import com.jofairden.discordkt.model.request.EditMessageBody
import com.jofairden.discordkt.model.request.GroupDMAddRecipientBody
import com.jofairden.discordkt.model.request.ModifyChannelBody
import com.jofairden.discordkt.util.lazyAsync
import java.util.Date

// TODO split in DMChannel / GuildChannel / TextChannel
data class DiscordChannel(
    @JsonProperty("id")
    val id: Long,
    @JsonProperty("type")
    val channelType: ChannelType,
    @JsonProperty("guild_id")
    val guildId: Long?,
    @JsonProperty("position")
    val position: Int?,
    @JsonProperty("permission_overwrites")
    val permissionOverwrites: Array<Overwrite>?,
    @JsonProperty("name")
    val name: String?,
    @JsonProperty("topic")
    val topic: String?,
    @JsonProperty("nsfw")
    val nsfw: Boolean?,
    @JsonProperty("last_message_id")
    val lastMessageId: Long?,
    @JsonProperty("bitrate")
    val bitrate: Int?,
    @JsonProperty("user_limit")
    val userLimit: Int?,
    @JsonProperty("rate_limit_per_user")
    val rateLimitPerUser: Int?,
    @JsonProperty("recipients")
    val recipients: Array<DiscordUser>?,
    @JsonProperty("icon")
    val iconHash: String?,
    @JsonProperty("owner_id")
    val ownerId: Long?,
    @JsonProperty("application_id")
    val applicationId: Long?,
    @JsonProperty("parent_id")
    val parentId: Long?,
    @JsonProperty("last_pin_timestamp")
    val lastPinTimestamp: Date?
) : ApiAware() {

    val guild by lazyAsync {
        if (guildId == null) null else dataCache.guildCache.getSuspending(guildId)
    }

    suspend fun getChannel() = dataCache.channelCache.getSuspending(id)

    suspend fun update(channel: ModifyChannelBody) = serviceProvider.channelService.modifyChannel(id, channel)

    suspend fun delete() = serviceProvider.channelService.deleteChannel(id)

    suspend fun getMessages(limit: Int = 50) = serviceProvider.channelService.getChannelMessages(id, limit)

    suspend fun getMessage(msgId: Long) = serviceProvider.channelService.getChannelMessage(id, msgId)

    suspend fun sendMessage(text: String, embed: MessageEmbed? = null) =
        serviceProvider.channelService.postChannelMessage(
            id,
            CreateMessageBody(content = text, embed = embed)
        )

    suspend fun addReaction(msgId: Long, emoji: IEmoji) =
        serviceProvider.channelService.createReaction(id, msgId, IEmoji.getText(emoji))

    suspend fun deleteReaction(msgId: Long, emoji: IEmoji) =
        serviceProvider.channelService.deleteReaction(id, msgId, IEmoji.getText(emoji))

    suspend fun deleteUserReaction(msgId: Long, emoji: IEmoji, userId: Long) =
        serviceProvider.channelService.deleteUserReaction(id, msgId, IEmoji.getText(emoji), userId)

    suspend fun getReactions(msgId: Long, emoji: IEmoji) =
        serviceProvider.channelService.getReactions(id, msgId, IEmoji.getText(emoji))

    suspend fun deleteAllReactions(msgId: Long) =
        serviceProvider.channelService.deleteAllReactions(id, msgId)

    suspend fun editMessage(msgId: Long, msgBody: EditMessageBody) =
        serviceProvider.channelService.editMessage(id, msgId, msgBody)

    suspend fun deleteMessage(msgId: Long) =
        serviceProvider.channelService.deleteMessage(id, msgId)

    suspend fun bulkDeleteMessages(messages: Array<Long>) =
        serviceProvider.channelService.bulkDeleteMessages(id, messages)

    suspend fun editChannelPermissions(overwriteId: Long, body: EditChannelPermissionsBody) =
        serviceProvider.channelService.editChannelPermissions(id, overwriteId, body)

    suspend fun getInvites() = serviceProvider.channelService.getChannelInvites(id)
    suspend fun getInvite(inviteId: Long) = serviceProvider.channelService.getChannelInvite(id, inviteId)
    suspend fun createInvite(body: CreateChannelInviteBody = CreateChannelInviteBody()) =
        serviceProvider.channelService.createChannelInvite(id, body)

    suspend fun deleteChannelPermission(overwriteId: Long) =
        serviceProvider.channelService.deleteChannelPermission(id, overwriteId)

    suspend fun triggerTypingIndicator() = serviceProvider.channelService.triggerTypingIndicator(id)
    suspend fun getPinnedMessages() = serviceProvider.channelService.getPinnedMessages(id)
    suspend fun addPinnedMessage(msgId: Long) = serviceProvider.channelService.addPinnedChannelMessage(id, msgId)
    suspend fun deletePinnedMessage(msgId: Long) = serviceProvider.channelService.deletePinnedChannelMessage(id, msgId)
    suspend fun addGroupDmRecipient(userId: Long, body: GroupDMAddRecipientBody) =
        serviceProvider.channelService.groupDMAddRecipient(id, userId, body)

    suspend fun deleteGroupDmRecipient(userId: Long) = serviceProvider.channelService.groupDMRemoveRecipient(id, userId)
}
