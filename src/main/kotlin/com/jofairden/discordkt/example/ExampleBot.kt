package com.jofairden.discordkt.example

import com.jofairden.discordkt.api.DiscordClient
import com.jofairden.discordkt.dsl.onChannelCreate
import com.jofairden.discordkt.dsl.onGuildMemberAdd
import com.jofairden.discordkt.dsl.onMessageCreate
import com.jofairden.discordkt.dsl.onMessageReactionAdd
import com.jofairden.discordkt.model.discord.emoji.DiscordEmoji
import com.jofairden.discordkt.model.discord.emoji.UnicodeEmoji
import com.jofairden.discordkt.model.discord.message.embed.EmbedAuthor
import com.jofairden.discordkt.model.discord.message.embed.EmbedField
import com.jofairden.discordkt.model.discord.message.embed.EmbedFooter
import com.jofairden.discordkt.model.discord.message.embed.EmbedThumbnail
import com.jofairden.discordkt.model.discord.message.embed.MessageEmbed
import com.jofairden.discordkt.model.request.CreateChannelInviteBody
import com.jofairden.discordkt.model.request.EditMessageBody
import mu.KotlinLogging
import java.io.File
import java.util.Date

class ExampleBot

// Run this method
fun main() {
    val logger = KotlinLogging.logger {}

    DiscordClient.buildAndRun(getBotToken()) {
        onMessageCreate { msg ->
            val split = msg.content!!.split(" ")
            val cmd = split[0].toLowerCase()
            if (cmd == ".channel") {
                val ch = msg.channel.await()
                val g = ch.guild.await()
                msg.reply(
                    "Channel info: ${ch.name}\nPart of guild: ${g?.name ?: "ERROR"}"
                )
            } else if (cmd == ".invite") {
                val ch = msg.channel.await()
                val inv = ch.createInvite(
                    CreateChannelInviteBody(
                        maxAgeInSeconds = 500,
                        maxUses = 5
                    )
                )
                msg.reply(
                    "Created invite that lasts for 500 seconds with 5 uses: ${inv.url}"
                )
            } else if (cmd == ".typing") {
                val ch = msg.channel.await()
                ch.triggerTypingIndicator()
            } else if (cmd == ".pinthis") {
                val ch = msg.channel.await()
                ch.addPinnedMessage(msg.id)
                msg.reply("Pinned message ${msg.id}")
            } else if (cmd == ".react") {
                if (split.size > 1) {
                    if (split[1].contains("<") && split[1].contains(">")) {
                        val emoji = split[1].replace("<", "").replace(">", "")
                        val lastIndex = emoji.lastIndexOf(':')
                        val name = emoji.substring(1, lastIndex)
                        val id = emoji.substring(lastIndex + 1, emoji.length - 1)
                        msg.react(DiscordEmoji(id = id.toLong(), name = name))
                    } else {
                        msg.react(UnicodeEmoji(name = split[1]))
                    }
                }
            } else if (cmd == ".nick") {
                if (split.size > 1) {
                    val ch = msg.channel.await()
                    val g = ch.guild.await()
                    g?.modifyCurrentUserNick(split[1])
                }
            } else if (cmd == ".owner") {
                val g = msg.guild.await()
                g?.owner?.await()?.let { user ->
                    msg.reply("Owner: ${user.username}")
                }
            }
        }
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
