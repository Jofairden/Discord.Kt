package com.jofairden.discordkt.api.cache

import com.github.benmanes.caffeine.cache.Caffeine
import com.jofairden.discordkt.model.discord.guild.Guild
import java.util.concurrent.TimeUnit

class DataCache {
    val guilds = Caffeine.newBuilder()
        .expireAfterAccess(5, TimeUnit.MINUTES)
        .maximumSize(10_000)
        .build<Long, Guild>()
}
