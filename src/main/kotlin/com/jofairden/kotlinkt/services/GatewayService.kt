package com.jofairden.kotlinkt.services

import com.fasterxml.jackson.databind.JsonNode
import retrofit2.http.GET

interface GatewayService {
    @GET("gateway")
    suspend fun getGateway(): JsonNode
}
