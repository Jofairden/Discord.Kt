package com.jofairden.discordkt.model.discord.emoji

interface IEmoji {
    val id: Long?
    val name: String?

    companion object {
        fun getText(emoji: IEmoji) = if (emoji is DiscordEmoji) {
            "${emoji.name}:${emoji.id}"
        } else {
            emoji.name!!
        }
    }
}
