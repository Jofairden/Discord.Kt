package com.jofairden.discordkt.model.request

import com.fasterxml.jackson.databind.JsonNode

data class CreateMessageBody(
    val content: String,
    val tts: Boolean = false,
    val embed: JsonNode? = null
)
