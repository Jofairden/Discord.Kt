package com.jofairden.discordkt.example.command

import com.jofairden.discordkt.api.command.ChatCommand
import com.jofairden.discordkt.api.command.CommandContext
import com.jofairden.discordkt.model.discord.emoji.DiscordEmoji
import com.jofairden.discordkt.model.discord.emoji.UnicodeEmoji
import com.jofairden.discordkt.model.discord.message.DiscordMessage
import com.jofairden.discordkt.model.discord.message.embed.EmbedAuthor
import com.jofairden.discordkt.model.discord.message.embed.EmbedField
import com.jofairden.discordkt.model.discord.message.embed.EmbedFooter
import com.jofairden.discordkt.model.discord.message.embed.EmbedThumbnail
import com.jofairden.discordkt.model.discord.message.embed.MessageEmbed
import com.jofairden.discordkt.model.request.CreateChannelInviteBody
import com.jofairden.discordkt.model.request.EditMessageBody
import java.util.Date

class SimpleCommands {
    class PingCommand : ChatCommand(text = "ping") {
        private var msg: DiscordMessage? = null

        override suspend fun CommandContext.execute(args: String) {
            msg = message.reply("Pong!")
        }

        override suspend fun CommandContext.postExecute() {
            msg?.edit(EditMessageBody(content = "Pong! (${Date().time - msg!!.timestamp.time}ms)"))
        }
    }

    class ChannelCommand : ChatCommand(text = "channel") {
        override suspend fun CommandContext.execute(args: String) {
            message.reply(
                "Channel info: ${channel.name}\nPart of guild: ${guild?.name ?: "ERROR"}"
            )
        }
    }

    class InviteCommand : ChatCommand(text = "invite") {
        override suspend fun CommandContext.execute(args: String) {
            val inv = channel.createInvite(
                CreateChannelInviteBody(
                    maxAgeInSeconds = 500,
                    maxUses = 5
                )
            )
            message.reply(
                "Created invite that lasts for 500 seconds with 5 uses: ${inv.url}"
            )
        }
    }

    class TypingCommand : ChatCommand(text = "typing") {
        override suspend fun CommandContext.execute(args: String) {
            channel.triggerTypingIndicator()
        }
    }

    class PinThisCommand : ChatCommand(text = "pinthis") {
        override suspend fun CommandContext.execute(args: String) {
            channel.addPinnedMessage(message.id)
            message.reply("Pinned message ${message.id}")
        }
    }

    class OwnerCommand : ChatCommand(text = "owner") {
        override suspend fun CommandContext.execute(args: String) {
            guild?.owner?.await()?.let { user ->
                message.reply("Owner: ${user.username}")
            }
        }
    }

    class NicknameCommand : ChatCommand(text = "nick") {
        override suspend fun CommandContext.execute(args: String) {
            val split = args.split(" ")
            if (split.size > 1) {
                guild?.modifyCurrentUserNick(split[1])
            }
        }
    }

    class ReactCommand : ChatCommand(text = "react") {
        override suspend fun CommandContext.execute(args: String) {
            val split = args.split(" ")
            if (split.size > 1) {
                if (split[1].contains("<") && split[1].contains(">")) {
                    val emoji = split[1].replace("<", "").replace(">", "")
                    val lastIndex = emoji.lastIndexOf(':')
                    val name = emoji.substring(1, lastIndex)
                    val id = emoji.substring(lastIndex + 1, emoji.length - 1)
                    message.react(DiscordEmoji(id = id.toLong(), name = name))
                } else {
                    message.react(UnicodeEmoji(name = split[1]))
                }
            }
        }
    }

    class MeCommand : ChatCommand(text = "me") {
        override suspend fun CommandContext.execute(args: String) {
            val roleField =
                if (message.authorGuildUser?.roles?.isNotEmpty() == true) message.authorGuildUser.roles.joinToString(",") { it.name }
                else "None"

            message.reply(
                MessageEmbed(
                    title = "User info: ${message.author.username}#${message.author.discriminator}",
                    author = EmbedAuthor(message.author.username),
                    thumbnail = EmbedThumbnail(message.author.avatarUrl),
                    fields = arrayOf(
                        EmbedField(
                            "Roles",
                            roleField,
                            inline = true
                        ),
                        EmbedField(
                            "ID",
                            message.author.id.toString(),
                            inline = true
                        )
                    ),
                    footer = EmbedFooter("Requested on ${Date()}")
                )
            )
        }
    }
}
