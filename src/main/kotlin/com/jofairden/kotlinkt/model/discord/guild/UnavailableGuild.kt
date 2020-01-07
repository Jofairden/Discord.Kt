package com.jofairden.kotlinkt.model.discord.guild

import com.fasterxml.jackson.annotation.JsonProperty

class UnavailableGuild(

    @JsonProperty("unavailable")
    val unavailable: Boolean,

    @JsonProperty("id")
    val id: String

)
