package com.jofairden.discordkt.model.api

import com.jofairden.discordkt.api.ApiServiceProvider
import com.jofairden.discordkt.api.cache.DataCache
import org.koin.core.KoinComponent
import org.koin.core.inject

abstract class ApiAware : KoinComponent {
    protected val serviceProvider by inject<ApiServiceProvider>()
    protected val dataCache by inject<DataCache>()
}
