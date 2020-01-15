package com.jofairden.discordkt.example

import com.jofairden.discordkt.api.DiscordClient
import com.jofairden.discordkt.dsl.onChannelCreate
import com.jofairden.discordkt.dsl.onGuildMemberAdd
import com.jofairden.discordkt.dsl.onMessageCreate
import com.jofairden.discordkt.dsl.onMessageReactionAdd
import com.jofairden.discordkt.model.discord.message.embed.EmbedAuthor
import com.jofairden.discordkt.model.discord.message.embed.EmbedField
import com.jofairden.discordkt.model.discord.message.embed.EmbedFooter
import com.jofairden.discordkt.model.discord.message.embed.EmbedThumbnail
import com.jofairden.discordkt.model.discord.message.embed.MessageEmbed
import com.jofairden.discordkt.model.request.EditMessageBody
import mu.KotlinLogging
import java.io.File
import java.util.Date

class ExampleBot

// Run this method
fun main() {
    val logger = KotlinLogging.logger {}

    DiscordClient.buildAndRun(getBotToken()) {

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
        onMessageCreate { msg ->
            if (msg.author.id == botUser.id
                && msg.content == "pong!"
            ) {
                with(msg) {
                    edit(
                        EditMessageBody(
                            "pong! (${Date().time - msg.timestamp.time} ms)"
                        )
                    )
                }
            } else if (msg.content == ".ping") {
                with(msg) {
                    reply("pong!")
                }
            } else if (msg.content == ".me") {
                with(msg) {
                    val roleField =
                        if (msg.authorGuildUser?.roles?.isNotEmpty() == true) msg.authorGuildUser.roles.joinToString(",") { it.name }
                        else "None"

                    reply(
                        MessageEmbed(
                            title = "User info: ${msg.author.username}#${msg.author.discriminator}",
                            author = EmbedAuthor(msg.author.username),
                            thumbnail = EmbedThumbnail(msg.author.avatarUrl),
                            fields = arrayOf(
                                EmbedField(
                                    "Roles",
                                    roleField,
                                    inline = true
                                ),
                                EmbedField("ID", msg.author.id.toString(), inline = true)
                            ),
                            footer = EmbedFooter("Requested on ${Date()}")
                        )
                    )
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
