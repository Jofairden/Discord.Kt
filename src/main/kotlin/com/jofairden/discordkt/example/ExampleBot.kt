package com.jofairden.discordkt.example

import com.jofairden.discordkt.api.DiscordClient
import com.jofairden.discordkt.dsl.onReady
import com.jofairden.discordkt.dsl.onTypingStart
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

        val token = getBotToken()
        DiscordClient.buildAndRun(token) {
            onReady {
                logger.info { "I am now ready! I am ${it.botUser.username}" }
            }
            onTypingStart {
                logger.info { "Typing ${it.user?.discordUser?.username}" }
            }
        }

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
