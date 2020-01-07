package com.jofairden.kotlinkt.example

import com.jofairden.kotlinkt.api.DiscordClient
import com.jofairden.kotlinkt.model.api.DiscordClientProperties
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import java.io.File

// Run this method
fun main() {
    ExampleBot().start()
}

class ExampleBot {
    private val logger = KotlinLogging.logger {}

    fun start() = runBlocking {
        val client = DiscordClient()
        with(client) {
            ready {
                logger.info { "I am now ready! I am ${it.botUser["username"].asText()}" }
            }
        }
        client.connect(
            // Create a file named "bot.token" in resources containing just the bot token for now
            DiscordClientProperties(
                getBotToken()
            )
        )
        delay(100000)
    }

    private fun getBotToken(): String {
        val classLoader = ExampleBot::class.java.classLoader
        val file = File(classLoader.getResource("bot.token").file)

        if (!file.canRead()) {
            throw RuntimeException("Unable to read bot.token file")
        }

        return file.readText()
    }
}
