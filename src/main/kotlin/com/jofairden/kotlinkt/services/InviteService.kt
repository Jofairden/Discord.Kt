package com.jofairden.kotlinkt.services

import com.fasterxml.jackson.databind.JsonNode
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Source: https://discordapp.com/developers/docs/resources/invite
 */
interface InviteService {
    @GET("invites/{invite.code}")
    suspend fun getInvite(
        @Path("invite.code") guildId: Int
    ): JsonNode // Invite

    @DELETE("invites/{invite.code}")
    suspend fun deleteInvite(
        @Path("invite.code") guildId: Int
    ): JsonNode
}
