package com.jofairden.kotlinkt.api

import com.jofairden.kotlinkt.model.DiscordClientProperties
import mu.KotlinLogging

class DiscordClient {

    private val logger = KotlinLogging.logger { }

    suspend fun connect(properties: DiscordClientProperties) {
        logger.info { "Test" }
    }
}
