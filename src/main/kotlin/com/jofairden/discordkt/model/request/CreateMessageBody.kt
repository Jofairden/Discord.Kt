package com.jofairden.discordkt.model.request

import com.jofairden.discordkt.model.discord.message.embed.MessageEmbed

data class CreateMessageBody(
    val content: String,
    val tts: Boolean = false,
    val embed: MessageEmbed? = null
)
