package com.jofairden.discordkt.model.context.event

import com.jofairden.discordkt.api.DiscordClient

interface IEventContext {
    var discordClient: DiscordClient
}
