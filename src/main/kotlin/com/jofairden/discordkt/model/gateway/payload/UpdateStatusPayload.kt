package com.jofairden.discordkt.model.gateway.payload

import com.jofairden.discordkt.model.discord.user.UserStatus
import com.jofairden.discordkt.model.discord.user.UserStatusActivity

data class UpdateStatusPayload(
    val idleTime: Int? = null,
    val activity: UserStatusActivity? = null,
    val status: UserStatus? = null,
    val isAfk: Boolean = false
) : WebsocketPayload()
