package com.jofairden.discordkt.model.api

import com.jofairden.discordkt.api.cache.DataCache

interface ICacheAware {
    val cache: DataCache
}
