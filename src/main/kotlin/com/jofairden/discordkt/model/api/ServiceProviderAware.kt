package com.jofairden.discordkt.model.api

import com.jofairden.discordkt.api.ApiServiceProvider
import org.koin.core.KoinComponent
import org.koin.core.inject

abstract class ServiceProviderAware : KoinComponent {
    protected val serviceProvider by inject<ApiServiceProvider>()
}
