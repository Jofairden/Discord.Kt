package com.jofairden.kotlinkt.api

import com.jofairden.kotlinkt.model.context.event.ReadyEventContext

typealias ReadyEventBlock = suspend (ctx: ReadyEventContext) -> Unit
