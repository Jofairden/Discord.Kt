package com.jofairden.discordkt.model.gateway

sealed class GatewayEvent(
    val name: String
) {
    object Hello : GatewayEvent("HELLO")
    object Ready : GatewayEvent("READY")
    object Resumed : GatewayEvent("RESUMED")
    object Reconnect : GatewayEvent("RECONNECT")
    object InvalidSession : GatewayEvent("INVALID_SESSION")
    object ChannelCreate : GatewayEvent("CHANNEL_CREATE")
    object ChannelUpdate : GatewayEvent("CHANNEL_UPDATE")
    object ChannelDelete : GatewayEvent("CHANNEL_DELETE")
    object ChannelPinsUpdate : GatewayEvent("CHANNEL_PINS_UPDATE")
    object GuildCreate : GatewayEvent("GUILD_CREATE")
    object GuildUpdate : GatewayEvent("GUILD_UPDATE")
    object GuildDelete : GatewayEvent("GUILD_DELETE")
    object GuildBanAdd : GatewayEvent("GUILD_BAN_ADD")
    object GuildBanRemove : GatewayEvent("GUILD_BAN_REMOVE")
    object GuildEmojisUpdate : GatewayEvent("GUILD_EMOJIS_UPDATE")
    object GuildIntegrationsUpdate : GatewayEvent("GUILD_INTEGRATIONS_UPDATE")
    object GuildMemberAdd : GatewayEvent("GUILD_MEMBER_ADD")
    object GuildMemberRemove : GatewayEvent("GUILD_MEMBER_REMOVE")
    object GuildMemberUpdate : GatewayEvent("GUILD_MEMBER_UPDATE")
    object GuildMembersChunk : GatewayEvent("GUILD_MEMBERS_CHUNK")
    object GuildRoleCreate : GatewayEvent("GUILD_ROLE_CREATE")
    object GuildRoleUpdate : GatewayEvent("GUILD_ROLE_UPDATE")
    object GuildRoleDelete : GatewayEvent("GUILD_ROLE_DELETE")
    object MessageCreate : GatewayEvent("MESSAGE_CREATE")
    object MessageUpdate : GatewayEvent("MESSAGE_UPDATE")
    object MessageDelete : GatewayEvent("MESSAGE_DELETE")
    object MessageDeleteBulk : GatewayEvent("MESSAGE_DELETE_BULK")
    object MessageReactionAdd : GatewayEvent("MESSAGE_REACTION_ADD")
    object MessageReactionRemove : GatewayEvent("MESSAGE_REACTION_REMOVE")
    object MessageReactionRemoveAll : GatewayEvent("MESSAGE_REACTION_REMOVE_ALL")
    object PresenceUpdate : GatewayEvent("PRESENCE_UPDATE")
    object TypingStart : GatewayEvent("TYPING_START")
    object UserUpdate : GatewayEvent("USER_UPDATE")
    object VoiceStateUpdate : GatewayEvent("VOICE_STATE_UPDATE")
    object VoiceServerUpdate : GatewayEvent("VOICE_SERVER_UPDATE")
    object WebhooksUpdate : GatewayEvent("WEBHOOKS_UPDATE")

    companion object {
        fun find(name: String) =
            GatewayEvent::class.sealedSubclasses.first { it.objectInstance?.name == name }.objectInstance
    }
}
