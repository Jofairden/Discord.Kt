package com.jofairden.discordkt.services

import com.jofairden.discordkt.model.discord.guild.GuildInvite
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Source: https://discordapp.com/developers/docs/resources/invite
 */
interface InviteService {
    @GET("invites/{invite-code}")
    suspend fun getInvite(
        @Path("invite-code") guildId: Long
    ): GuildInvite // Invite

    @DELETE("invites/{invite-code}")
    suspend fun deleteInvite(
        @Path("invite-code") guildId: Long
    ): GuildInvite
}
