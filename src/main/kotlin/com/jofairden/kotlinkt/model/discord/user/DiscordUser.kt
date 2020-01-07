package com.jofairden.kotlinkt.model.discord.user

import com.fasterxml.jackson.annotation.JsonProperty

class DiscordUser(

    @JsonProperty("id")
    val snowflakeId: String,

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

    @JsonProperty("flags")
    val flags: UserFlags?,

    @JsonProperty("premium_type")
    val nitroType: NitroType?
)
