package com.jofairden.discordkt.model.api

import com.jofairden.discordkt.api.ApiServiceProvider

interface ISPAware {
    val serviceProvider: ApiServiceProvider
}
