package com.jofairden.discordkt.api

import com.fasterxml.jackson.databind.JsonNode
import com.jofairden.discordkt.model.context.event.ChannelPinsUpdateEventContext
import com.jofairden.discordkt.model.context.event.GuildBanEventContext
import com.jofairden.discordkt.model.context.event.GuildEmojisUpdateEventContext
import com.jofairden.discordkt.model.context.event.GuildIdEventContext
import com.jofairden.discordkt.model.context.event.GuildMemberAddEventContext
import com.jofairden.discordkt.model.context.event.GuildMemberRemoveEventContext
import com.jofairden.discordkt.model.context.event.GuildMemberUpdateEventContext
import com.jofairden.discordkt.model.context.event.GuildMembersChunkEventContext
import com.jofairden.discordkt.model.context.event.GuildRoleEventContext
import com.jofairden.discordkt.model.context.event.GuildRoleIdEventContext
import com.jofairden.discordkt.model.context.event.MessageDeleteBulkEventContext
import com.jofairden.discordkt.model.context.event.MessageDeleteEventContext
import com.jofairden.discordkt.model.context.event.MessageReactionAddEventContext
import com.jofairden.discordkt.model.context.event.MessageReactionRemoveAllEventContext
import com.jofairden.discordkt.model.context.event.MessageReactionRemoveEventContext
import com.jofairden.discordkt.model.context.event.ReadyEventContext
import com.jofairden.discordkt.model.context.event.TypingStartEventContext
import com.jofairden.discordkt.model.discord.guild.Guild
import com.jofairden.discordkt.model.discord.guild.UnavailableGuild
import com.jofairden.discordkt.model.discord.user.DiscordUser

typealias EventContextBlock<T> = suspend (ctx: T) -> Unit

typealias ReadyEventBlock = EventContextBlock<ReadyEventContext>
typealias NoArgsEventBlock = suspend () -> Unit
typealias InvalidSessionEventBlock = suspend (mayResume: Boolean) -> Unit
typealias ChannelEventBlock = suspend (channel: JsonNode) -> Unit // Channel
// typealias ChannelUpdateEventBlock = suspend(channel: JsonNode) -> Unit // Channel
// typealias ChannelDeleteEventBlock = suspend(channel: JsonNode) -> Unit // Channel
typealias ChannelPinsUpdateEventBlock = EventContextBlock<ChannelPinsUpdateEventContext>

typealias GuildEventBlock = suspend (guild: Guild) -> Unit
// typealias GuildUpdateEventBlock = suspend(guild: Guild) -> Unit
typealias GuildDeleteEventBlock = suspend (guild: UnavailableGuild, userWasRemoved: Boolean) -> Unit

typealias GuildBanEventBlock = EventContextBlock<GuildBanEventContext>
typealias GuildEmojisUpdateEventBlock = EventContextBlock<GuildEmojisUpdateEventContext>
typealias GuildIdEventBlock = EventContextBlock<GuildIdEventContext>
typealias GuildMemberAddEventBlock = EventContextBlock<GuildMemberAddEventContext>
typealias GuildMemberRemoveEventBlock = EventContextBlock<GuildMemberRemoveEventContext>
typealias GuildMemberUpdateEventBlock = EventContextBlock<GuildMemberUpdateEventContext>
typealias GuildMembersChunkEventBlock = EventContextBlock<GuildMembersChunkEventContext>
typealias GuildRoleCreateEventBlock = EventContextBlock<GuildRoleEventContext>
typealias GuildRoleUpdateEventBlock = EventContextBlock<GuildRoleEventContext>
typealias GuildRoleDeleteEventBlock = EventContextBlock<GuildRoleIdEventContext>
typealias ChannelMessageEventBlock = suspend (message: JsonNode) -> Unit // Message
typealias MessageCreateEventBlock = ChannelMessageEventBlock
typealias MessageUpdateEventBlock = ChannelMessageEventBlock
typealias MessageDeleteEventBlock = EventContextBlock<MessageDeleteEventContext>
typealias MessageDeleteBulkEventBlock = EventContextBlock<MessageDeleteBulkEventContext>
typealias MessageReactionAddEventBlock = EventContextBlock<MessageReactionAddEventContext>
typealias MessageReactionRemoveEventBlock = EventContextBlock<MessageReactionRemoveEventContext>
typealias MessageReactionRemoveAllEventBlock = EventContextBlock<MessageReactionRemoveAllEventContext>
typealias TypingStartEventBlock = EventContextBlock<TypingStartEventContext>
typealias UserUpdateEventBlock = suspend (user: DiscordUser) -> Unit
