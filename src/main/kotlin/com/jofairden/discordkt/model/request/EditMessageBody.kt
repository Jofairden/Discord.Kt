package com.jofairden.discordkt.model.request

import com.jofairden.discordkt.model.discord.message.embed.MessageEmbed

data class EditMessageBody(
    val content: String? = null,
    val embed: MessageEmbed? = null,
    val flags: Int? = null
)
