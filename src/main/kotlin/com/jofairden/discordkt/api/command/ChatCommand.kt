package com.jofairden.discordkt.api.command

import com.jofairden.discordkt.model.discord.channel.DiscordChannel
import com.jofairden.discordkt.model.discord.guild.Guild
import com.jofairden.discordkt.model.discord.message.DiscordMessage

abstract class ChatCommand(
    private val prefix: String = ".",
    private val text: String
) {
    abstract suspend fun CommandContext.execute(args: String)
    open suspend fun CommandContext.postExecute() {}

    fun matches(match: String) = match == "${prefix}$text"
}

data class CommandContext(
    val message: DiscordMessage,
    val channel: DiscordChannel,
    val guild: Guild?
)
