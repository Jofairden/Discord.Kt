package com.jofairden.kotlinkt.example

import com.jofairden.kotlinkt.api.DiscordClient
import com.jofairden.kotlinkt.model.DiscordClientProperties
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
	val client = DiscordClient()
	client.connect(
		DiscordClientProperties(
			"",
			""
		)
	)
}