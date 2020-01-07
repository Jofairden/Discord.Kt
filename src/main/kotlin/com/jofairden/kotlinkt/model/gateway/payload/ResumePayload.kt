package com.jofairden.kotlinkt.model.gateway.payload

data class ResumePayload(
    val token: String,
    val session_id: String,
    val seq: Int
) : WebsocketPayload()
