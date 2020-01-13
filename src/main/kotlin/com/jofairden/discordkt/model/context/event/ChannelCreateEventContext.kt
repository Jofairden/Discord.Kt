package com.jofairden.discordkt.model.context.event

import com.jofairden.discordkt.api.DiscordClient
import com.jofairden.discordkt.model.discord.channel.DiscordChannel
import com.jofairden.discordkt.model.discord.message.embed.MessageEmbed
import com.jofairden.discordkt.model.request.CreateMessageBody

data class ChannelCreateEventContext(
    private val client: DiscordClient,
    val channel: DiscordChannel
) {
    suspend fun sendMessage(text: String) {
        client.serviceProvider.channelService.postChannelMessage(
            channel.id,
            CreateMessageBody(text)
        )
    }

    suspend fun DiscordClient.sendMessage(embed: MessageEmbed) {
        this.serviceProvider.channelService.postChannelMessage(
            channel.id,
            CreateMessageBody("", embed = embed)
        )
    }
}
