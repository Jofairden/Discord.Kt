package com.jofairden.discordkt.model.discord.guild

import com.fasterxml.jackson.annotation.JsonValue

enum class GuildFeature(
    @JsonValue
    val feature: String
) {
    InviteSplash("INVITE_SPLASH"),
    VipRegions("VIP_REGIONS"),
    VanityUrl("VANITY_URL"),
    Verified("VERIFIED"),
    Partnered("PARTNERED"),
    Public("PUBLIC"),
    Commerce("COMMERCE"),
    News("NEWS"),
    Discoverable("DISCOVERABLE"),
    Featurable("FEATURABLE"),
    AnimatedIcon("ANIMATED_ICON"),
    Banner("BANNER")
}
