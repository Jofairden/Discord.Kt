package com.jofairden.discordkt.model.request

import com.fasterxml.jackson.annotation.JsonProperty

data class GroupDMAddRecipientBody(
    @JsonProperty("access_token")
    val accesToken: String,
    @JsonProperty("nick")
    val nick: String
)
