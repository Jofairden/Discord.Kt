package com.jofairden.discordkt.api

import com.jofairden.discordkt.model.api.DiscordClientProperties
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

/**
 * The ClientAuthenticator is called when a 401 Unauthorized is returned
 * We will attempt to add the required Authorization header and continue
 * If the header is already present we will fail
 */
class DiscordClientAuthenticator(
    private val properties: DiscordClientProperties
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        val requiredHeader = properties.getAuthorizationHeader()

        if (requiredHeader == response.request.header("Authorization")) {
            return null
        }

        return response.request.newBuilder()
            .header("Authorization", requiredHeader)
            .build()
    }
}
