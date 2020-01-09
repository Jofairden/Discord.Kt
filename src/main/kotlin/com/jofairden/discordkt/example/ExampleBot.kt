package com.jofairden.discordkt.example

import com.jofairden.discordkt.api.DiscordClient
import com.jofairden.discordkt.dsl.onMessageCreate
import java.io.File

class ExampleBot

// Run this method
fun main() {
    DiscordClient.buildAndRun(getBotToken()) {
        onMessageCreate { msg ->
            if (msg.content ?: "" == ".ping") {
                with(msg) {
                    reply("pong!")
                }
            }
        }
    }
}

private fun getBotToken(): String {
    val classLoader = ExampleBot::class.java.classLoader
    val res = classLoader.getResource("bot.token") ?: throw java.lang.RuntimeException("Resource bot.token not found")
    val file = File(res.file)

    if (!file.canRead()) {
        throw RuntimeException("Unable to read bot.token file")
    }

    return file.readText()
}
