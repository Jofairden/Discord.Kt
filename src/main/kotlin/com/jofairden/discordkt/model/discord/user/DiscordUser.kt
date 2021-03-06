package com.jofairden.discordkt.model.discord.user

import com.fasterxml.jackson.annotation.JsonProperty

class DiscordUser(

    @JsonProperty("id")
    val id: Long,

    @JsonProperty("username")
    val username: String,

    @JsonProperty("discriminator")
    val discriminator: String,

    @JsonProperty("avatar")
    val avatarHash: String?,

    @JsonProperty("bot")
    val isBotUser: Boolean?,

    @JsonProperty("system")
    val isSystemUser: Boolean?,

    @JsonProperty("mfa_enabled")
    val usesMfA: Boolean?,

    @JsonProperty("locale")
    val locale: String?,

    @JsonProperty("verified")
    val isEmailVerified: Boolean?,

    @JsonProperty("email")
    val email: String?,

    @JsonProperty("premium_type")
    val nitroType: NitroType?,

    @JsonProperty("public_flags")
    val flags: Int? // TODO
) {

    val avatarUrl = "https://cdn.discordapp.com/avatars/$id/$avatarHash.png"
}
