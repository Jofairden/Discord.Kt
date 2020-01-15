package com.jofairden.discordkt.model.api

import com.jofairden.discordkt.api.cache.DataCache

abstract class CacheAware : ICacheAware {
    override lateinit var cache: DataCache
}
