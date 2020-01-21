package com.jofairden.discordkt.model.request

data class ModifyBotUserBody(
    val username: String,
    val avatar: String // TODO image_data https://discordapp.com/developers/docs/reference#image-data
)
