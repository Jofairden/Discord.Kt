package com.jofairden.discordkt.example

import com.jofairden.discordkt.api.DiscordClient
import com.jofairden.discordkt.dsl.onChannelCreate
import com.jofairden.discordkt.dsl.onGuildMemberAdd
import com.jofairden.discordkt.dsl.onMessageReactionAdd
import com.jofairden.discordkt.example.command.SimpleCommands
import mu.KotlinLogging
import java.io.File

class ExampleBot

// Run this method
fun main() {
    val logger = KotlinLogging.logger {}

    DiscordClient.buildAndRun(getBotToken()) {
        registerCommands(SimpleCommands::class.java)

        onMessageReactionAdd {
            val channel = channel.await()
            if (this.emoji.name == "\uD83D\uDC4C") {
                channel.sendMessage("I saw you looking")
            }
        }
        onChannelCreate {
            if (channel.guildId != null) {
                sendMessage("Hey, a new channel?")
            }
        }
        onGuildMemberAdd {
            val g = guild.await()
            logger.info { "${user.discordUser?.username} joined guild ${g.name}" }
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
