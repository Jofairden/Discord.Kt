package com.jofairden.kotlinkt.services

import com.fasterxml.jackson.databind.JsonNode
import retrofit2.http.GET

/**
 * Source: https://discordapp.com/developers/docs/topics/gateway#get-gateway
 */
interface GatewayService {
    @GET("gateway")
    suspend fun getGateway(): JsonNode
}
