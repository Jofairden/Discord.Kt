package com.jofairden.kotlinkt.services

import com.fasterxml.jackson.databind.JsonNode
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Source: https://discordapp.com/developers/docs/resources/audit-log
 */
interface AuditLogService {
    @GET("guilds/{guild.id}/audit-logs")
    suspend fun getAuditLog(
        @Path("guild.id") guildId: Int
    ): JsonNode // Audit Log
}
