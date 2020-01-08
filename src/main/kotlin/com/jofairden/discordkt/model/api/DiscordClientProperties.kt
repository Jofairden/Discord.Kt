package com.jofairden.discordkt.model.api

data class DiscordClientProperties(
    val token: String,
    val authType: ClientAuthType = ClientAuthType.Bot
) {
    fun getAuthorizationHeader() = "${authType} ${token}"
}

