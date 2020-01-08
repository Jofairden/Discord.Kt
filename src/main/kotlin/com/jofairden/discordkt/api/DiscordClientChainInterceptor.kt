package com.jofairden.discordkt.api

import com.jofairden.discordkt.model.api.DiscordClientProperties
import okhttp3.Interceptor
import okhttp3.Response

/**
 * The DiscordClientChainInterceptor is immediately called when the http client serves a request
 * We will attempt to add the required headers to the request before it goes out
 */
class DiscordClientChainInterceptor(
    private val properties: DiscordClientProperties
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return if (chain.request().url.toString().startsWith(ApiServiceProvider.API_URL)) {
            val newReq = chain.request().newBuilder()
                .addHeader("Authorization", properties.getAuthorizationHeader())
                .build()
            chain.proceed(newReq)
        } else {
            chain.proceed(chain.request())
        }
    }
}
