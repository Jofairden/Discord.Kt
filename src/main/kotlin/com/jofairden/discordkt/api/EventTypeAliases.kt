package com.jofairden.discordkt.api

import com.jofairden.discordkt.model.context.event.ReadyEventContext

typealias ReadyEventBlock = suspend (ctx: ReadyEventContext) -> Unit
