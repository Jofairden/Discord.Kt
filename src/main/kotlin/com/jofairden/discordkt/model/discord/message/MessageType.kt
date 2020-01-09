package com.jofairden.discordkt.model.discord.message

import com.fasterxml.jackson.annotation.JsonValue

enum class MessageType(
    @JsonValue
    val value: Int
) {
    Default(0),
    RecipientAdd(1),
    RecipientRemove(2),
    Call(3),
    ChannelNameChange(4),
    ChannelIconChange(5),
    ChannelPinnedMessage(6),
    GuildMemberJoin(7),
    UserPremiumGuildSubscription(8),
    UserPremiumGuildSubscriptionTier1(9),
    UserPremiumGuildSubscriptionTier2(10),
    UserPremiumGuildSubscriptionTier3(11),
    ChannelFollowAdd(12)
}
