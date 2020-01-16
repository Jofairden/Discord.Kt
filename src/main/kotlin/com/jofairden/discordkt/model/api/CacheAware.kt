package com.jofairden.discordkt.model.api

import com.jofairden.discordkt.api.cache.DataCache
import org.koin.core.KoinComponent
import org.koin.core.inject

abstract class CacheAware : KoinComponent {
    protected val cache by inject<DataCache>()
}
