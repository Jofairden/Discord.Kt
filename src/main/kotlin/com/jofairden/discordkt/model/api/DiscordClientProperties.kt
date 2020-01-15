package com.jofairden.discordkt.model.api

data class DiscordClientProperties(
    val token: String,
    val authType: ClientAuthType = ClientAuthType.Bot,
    val dataCacheProperties: DataCacheProperties = DataCacheProperties()
) {
    fun getAuthorizationHeader() = "${authType} ${token}"
}

data class DataCacheProperties(
    val guildCacheMaxSize: Long = 1_000,
    val guildUsersCacheMaxSize: Long = 10_000,
    val userCacheMaxSize: Long = 10_000,
    val guildRolesCacheMaxSize: Long = 1_000,
    val emojiCacheMaxSize: Long = 10_000,
    val messageCacheMaxSize: Long = 1_000,
    val channelCacheMaxSize: Long = 1_000
)
