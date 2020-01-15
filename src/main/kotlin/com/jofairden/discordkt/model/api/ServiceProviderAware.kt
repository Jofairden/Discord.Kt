package com.jofairden.discordkt.model.api

import com.jofairden.discordkt.api.ApiServiceProvider

abstract class ServiceProviderAware : ISPAware {
    override lateinit var serviceProvider: ApiServiceProvider
}
