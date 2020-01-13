package com.jofairden.discordkt.dsl

import com.jofairden.discordkt.api.ChannelCreateEventBlock
import com.jofairden.discordkt.api.ChannelEventBlock
import com.jofairden.discordkt.api.ChannelPinsUpdateEventBlock
import com.jofairden.discordkt.api.DiscordClient
import com.jofairden.discordkt.api.DiscordClientDsl
import com.jofairden.discordkt.api.GuildBanEventBlock
import com.jofairden.discordkt.api.GuildEmojisUpdateEventBlock
import com.jofairden.discordkt.api.GuildEventBlock
import com.jofairden.discordkt.api.GuildIdEventBlock
import com.jofairden.discordkt.api.GuildMemberAddEventBlock
import com.jofairden.discordkt.api.GuildMemberRemoveEventBlock
import com.jofairden.discordkt.api.GuildMemberUpdateEventBlock
import com.jofairden.discordkt.api.GuildMembersChunkEventBlock
import com.jofairden.discordkt.api.GuildRoleCreateEventBlock
import com.jofairden.discordkt.api.GuildRoleDeleteEventBlock
import com.jofairden.discordkt.api.GuildRoleUpdateEventBlock
import com.jofairden.discordkt.api.InvalidSessionEventBlock
import com.jofairden.discordkt.api.MessageCreateEventBlock
import com.jofairden.discordkt.api.MessageDeleteBulkEventBlock
import com.jofairden.discordkt.api.MessageDeleteEventBlock
import com.jofairden.discordkt.api.MessageReactionAddEventBlock
import com.jofairden.discordkt.api.MessageReactionRemoveAllEventBlock
import com.jofairden.discordkt.api.MessageReactionRemoveEventBlock
import com.jofairden.discordkt.api.MessageUpdateEventBlock
import com.jofairden.discordkt.api.NoArgsEventBlock
import com.jofairden.discordkt.api.ReadyEventBlock
import com.jofairden.discordkt.api.TypingStartEventBlock
import com.jofairden.discordkt.api.UserUpdateEventBlock

@DiscordClientDsl
fun DiscordClient.onReady(block: ReadyEventBlock) {
    readyEventHandlers += block
}

@DiscordClientDsl
fun DiscordClient.onResumed(block: NoArgsEventBlock) {
    resumedEventBlocks += block
}

@DiscordClientDsl
fun DiscordClient.onInvalidSession(block: InvalidSessionEventBlock) {
    invalidSessionEventBlocks += block
}

@DiscordClientDsl
fun DiscordClient.onChannelCreate(block: ChannelCreateEventBlock) {
    channelCreateEventBlocks += block
}

@DiscordClientDsl
fun DiscordClient.onChannelUpdate(block: ChannelEventBlock) {
    channelUpdateEventBlocks += block
}

@DiscordClientDsl
fun DiscordClient.onChannelDelete(block: ChannelEventBlock) {
    channelDeleteEventBlocks += block
}

@DiscordClientDsl
fun DiscordClient.onChannelPinsUpdate(block: ChannelPinsUpdateEventBlock) {
    channelPinsUpdateEventBlocks += block
}

@DiscordClientDsl
fun DiscordClient.onGuildCreate(block: GuildEventBlock) {
    guildCreateEventBlocks += block
}

@DiscordClientDsl
fun DiscordClient.onGuildUpdate(block: GuildEventBlock) {
    guildUpdateEventBlocks += block
}

@DiscordClientDsl
fun DiscordClient.onGuildBanAdd(block: GuildBanEventBlock) {
    guildBanAddEventBlocks += block
}

@DiscordClientDsl
fun DiscordClient.onGuildBanRemove(block: GuildBanEventBlock) {
    guildBanRemoveEventBlocks += block
}

@DiscordClientDsl
fun DiscordClient.onGuildEmojisUpdate(block: GuildEmojisUpdateEventBlock) {
    guildEmojisUpdateEventBlocks += block
}

@DiscordClientDsl
fun DiscordClient.onGuildIntegrationsUpdate(block: GuildIdEventBlock) {
    guildIntegrationsUpdateEventBlocks += block
}

@DiscordClientDsl
fun DiscordClient.onGuildMemberAdd(block: GuildMemberAddEventBlock) {
    guildMemberAddEventBlocks += block
}

@DiscordClientDsl
fun DiscordClient.onGuildMemberRemove(block: GuildMemberRemoveEventBlock) {
    guildMemberRemoveEventBlocks += block
}

@DiscordClientDsl
fun DiscordClient.onGuildMemberUpdate(block: GuildMemberUpdateEventBlock) {
    guildMemberUpdateEventBlocks += block
}

@DiscordClientDsl
fun DiscordClient.onGuildMembersChunk(block: GuildMembersChunkEventBlock) {
    guildMembersChunkEventBlocks += block
}

@DiscordClientDsl
fun DiscordClient.onGuildRoleCreate(block: GuildRoleCreateEventBlock) {
    guildRoleCreateEventBlocks += block
}

@DiscordClientDsl
fun DiscordClient.onGuildRoleUpdate(block: GuildRoleUpdateEventBlock) {
    guildRoleUpdateEventBlocks += block
}

@DiscordClientDsl
fun DiscordClient.onGuildRoleDelete(block: GuildRoleDeleteEventBlock) {
    guildRoleDeleteEventBlocks += block
}

@DiscordClientDsl
fun DiscordClient.onMessageCreate(block: MessageCreateEventBlock) {
    messageCreateEventBlocks += block
}

@DiscordClientDsl
fun DiscordClient.onMessageUpdate(block: MessageUpdateEventBlock) {
    messageUpdateEventBlocks += block
}

@DiscordClientDsl
fun DiscordClient.onMessageDelete(block: MessageDeleteEventBlock) {
    messageDeleteEventBlocks += block
}

@DiscordClientDsl
fun DiscordClient.onMessageDeleteBulk(block: MessageDeleteBulkEventBlock) {
    messageDeleteBulkEventBlocks += block
}

@DiscordClientDsl
fun DiscordClient.onMessageReactionAdd(block: MessageReactionAddEventBlock) {
    messageReactionAddEventBlocks += block
}

@DiscordClientDsl
fun DiscordClient.onMessageReactionRemove(block: MessageReactionRemoveEventBlock) {
    messageReactionRemoveEventBlocks += block
}

@DiscordClientDsl
fun DiscordClient.onMessageReactionRemoveAll(block: MessageReactionRemoveAllEventBlock) {
    messageReactionRemoveAllEventBlocks += block
}

@DiscordClientDsl
fun DiscordClient.onTypingStart(block: TypingStartEventBlock) {
    typingStartEventBlocks += block
}

@DiscordClientDsl
fun DiscordClient.onUserUpdate(block: UserUpdateEventBlock) {
    userUpdateEventBlocks += block
}

