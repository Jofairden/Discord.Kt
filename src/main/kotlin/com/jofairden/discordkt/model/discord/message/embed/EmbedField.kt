package com.jofairden.discordkt.model.discord.message.embed

data class EmbedField(
    val name: String,
    val value: String,
    val inline: Boolean? = null
)
