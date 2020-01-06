package com.jofairden.kotlinkt.example

import com.jofairden.kotlinkt.api.DiscordClient
import com.jofairden.kotlinkt.model.DiscordClientProperties
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.io.File

// Run this method
fun main() {
    ExampleBot().start()
}

class ExampleBot {
    fun start() = runBlocking {
        val client = DiscordClient()
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
