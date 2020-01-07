package com.jofairden.kotlinkt.model.gateway

sealed class GatewayEvent {
    object Hello : GatewayEvent()
    object Ready : GatewayEvent()
    object Resumed : GatewayEvent()
    object Reconnect : GatewayEvent()
    object InvalidSession : GatewayEvent()
    object ChannelCreate : GatewayEvent()
    object ChannelUpdate : GatewayEvent()
    object ChannelDelete : GatewayEvent()
    object ChannelPinsUpdate : GatewayEvent()
    object GuildCreate : GatewayEvent()
    object GuildUpdate : GatewayEvent()
    object GuildDelete : GatewayEvent()
    object GuildBanAdd : GatewayEvent()
    object GuildBanRemove : GatewayEvent()
    object GuildEmojisUpdate : GatewayEvent()
    object GuildIntegrationsUpdate : GatewayEvent()
    object GuildMemberAdd : GatewayEvent()
    object GuildMemberRemove : GatewayEvent()
    object GuildMemberUpdate : GatewayEvent()
    object GuildMembersChunk : GatewayEvent()
    object GuildRoleCreate : GatewayEvent()
    object GuildRoleUpdate : GatewayEvent()
    object GuildRoleDelete : GatewayEvent()
    object MessageCreate : GatewayEvent()
    object MessageUpdate : GatewayEvent()
    object MessageDelete : GatewayEvent()
    object MessageDeleteBulk : GatewayEvent()
    object MessageReactionAdd : GatewayEvent()
    object MessageReactionRemove : GatewayEvent()
    object MessageReactionRemoveAll : GatewayEvent()
    object PresenceUpdate : GatewayEvent()
    object TypingStart : GatewayEvent()
    object UserUpdate : GatewayEvent()
    object VoiceStateUpdate : GatewayEvent()
    object VoiceServerUpdate : GatewayEvent()
    object WebhooksUpdate : GatewayEvent()
}
