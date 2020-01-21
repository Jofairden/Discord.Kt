package com.jofairden.discordkt.services

import com.jofairden.discordkt.model.gateway.GatewayUrl
import retrofit2.http.GET

/**
 * Source: https://discordapp.com/developers/docs/topics/gateway#get-gateway
 */
interface GatewayService {
    @GET("gateway")
    suspend fun getGateway(): GatewayUrl
}
