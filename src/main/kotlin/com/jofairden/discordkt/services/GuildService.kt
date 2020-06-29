package com.jofairden.discordkt.services

import com.fasterxml.jackson.databind.JsonNode
import com.jofairden.discordkt.model.discord.channel.DiscordChannel
import com.jofairden.discordkt.model.discord.guild.Guild
import com.jofairden.discordkt.model.discord.guild.GuildEmbed
import com.jofairden.discordkt.model.discord.guild.GuildInvite
import com.jofairden.discordkt.model.discord.guild.GuildPruneCount
import com.jofairden.discordkt.model.discord.guild.GuildUser
import com.jofairden.discordkt.model.discord.role.GuildRole
import com.jofairden.discordkt.model.request.CreateGuildChannelBody
import com.jofairden.discordkt.model.request.CreateGuildRoleBody
import com.jofairden.discordkt.model.request.ModifyGuildBody
import com.jofairden.discordkt.model.request.ModifyGuildMemberBody
import com.jofairden.discordkt.model.request.ModifyGuildRolePositionBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Source: https://discordapp.com/developers/docs/resources/guild
 */
interface GuildService {

    // TODO bots can't do this
    // @POST("guilds")
    // suspend fun createGuild(): Guild

    @GET("guilds/{guild-id}")
    suspend fun getGuild(
        @Path("guild-id") guildId: Long
    ): Guild

    @PATCH("guilds/{guild-id}")
    suspend fun modifyGuild(
        @Path("guild-id") guildId: Long,
        @Body body: ModifyGuildBody
    ): Guild

    // TODO bots can't do this
    // @DELETE("guilds/{guild-id}")
    // suspend fun deleteGuild(
    //     @Path("guild-id") guildId: Long
    // ): Response<Unit>

    @GET("guilds/{guild-id}/channels")
    suspend fun getGuildChannels(
        @Path("guild-id") guildId: Long
    ): ArrayList<DiscordChannel> // Channel

    // MANAGE_CHANNELS
    // TODO body
    @POST("guilds/{guild-id}/channels")
    suspend fun createGuildChannel(
        @Path("guild-id") guildId: Long,
        @Body body: CreateGuildChannelBody
    ): DiscordChannel // Channel

    // MANAGE_CHANNELS
    @PATCH("guilds/{guild-id}/channels")
    suspend fun modifyGuildChannelPositions(
        @Path("guild-id") guildId: Long
    ): Response<Unit> // 204 Empty

    @GET("guilds/{guild-id}/members/{user-id}")
    suspend fun getGuildMember(
        @Path("guild-id") guildId: Long,
        @Path("user-id") userId: Long
    ): GuildUser // Guild Member

    @GET("guilds/{guild-id}/members")
    suspend fun getGuildMembers(
        @Path("guild-id") guildId: Long,
        @Query("limit") limit: Int? = null,
        @Query("after") after: Long? = null
    ): Array<GuildUser> // Guild Member

    // TODO
    // @PUT("guilds/{guild-id}/members/{user-id}")
    // suspend fun addGuildMember(
    //     @Path("guild-id") guildId: Long,
    //     @Path("user-id") userId: Long
    // ): Response<Unit> // 201 Created // 204 No content

    @PATCH("guilds/{guild-id}/members/{user-id}")
    suspend fun modifyGuildMember(
        @Path("guild-id") guildId: Long,
        @Path("user-id") userId: Long,
        @Body body: ModifyGuildMemberBody
    ): Response<Unit>

    @FormUrlEncoded
    @PATCH("guilds/{guild-id}/members/@me/nick")
    suspend fun modifyCurrentUserNick(
        @Path("guild-id") guildId: Long,
        @Field("nick") nick: String
    ): Response<Unit> // 200 with nick

    // MANAGE_ROLES
    @PUT("guilds/{guild-id}/members/{user-id}/roles/{role-id}")
    suspend fun addGuildMemberRole(
        @Path("guild-id") guildId: Long,
        @Path("user-id") userId: Long,
        @Path("role-id") roleId: Long
    ): Response<Unit>

    // MANAGE_ROLES
    @DELETE("guilds/{guild-id}/members/{user-id}/roles/{role-id}")
    suspend fun removeGuildMemberRole(
        @Path("guild-id") guildId: Long,
        @Path("user-id") userId: Long,
        @Path("role-id") roleId: Long
    ): Response<Unit>

    // KICK_MEMBERS
    @DELETE("guilds/{guild-id}/members/{user-id}")
    suspend fun removeGuildMember(
        @Path("guild-id") guildId: Long,
        @Path("user-id") userId: Long
    ): Response<Unit>

    // BAN_MEMBERS
    @GET("guilds/{guild-id}/bans")
    suspend fun getGuildBans(
        @Path("guild-id") guildId: Long
    ): ArrayList<JsonNode> // Ban

    // BAN_MEMBERS
    @GET("guilds/{guild-id}/bans/{user-id}")
    suspend fun getGuildBan(
        @Path("guild-id") guildId: Long,
        @Path("user-id") userId: Long
    ): JsonNode // Ban//404

    // BAN_MEMBERS
    @PUT("guilds/{guild-id}/bans/{user-id}")
    suspend fun createGuildBan(
        @Path("guild-id") guildId: Long,
        @Path("user-id") userId: Long,
        @Query("delete-message-days") deleteMessageDays: Int? = null,
        @Query("reason") reason: String? = null
    ): JsonNode // Ban//404

    // BAN_MEMBERS
    @DELETE("guilds/{guild-id}/bans/{user-id}")
    suspend fun removeGuildBan(
        @Path("guild-id") guildId: Long,
        @Path("user-id") userId: Long
    ): Response<Unit> // Ban//404

    @GET("guilds/{guild-id}/roles")
    suspend fun getGuildRoles(
        @Path("guild-id") guildId: Long
    ): Array<GuildRole>

    // MANAGE_ROLES
    @POST("guilds/{guild-id}/roles")
    suspend fun createGuildRole(
        @Path("guild-id") guildId: Long,
        @Body body: CreateGuildRoleBody
    ): GuildRole

    // MANAGE_ROLES
    @FormUrlEncoded
    @PATCH("guilds/{guild-id}/roles")
    suspend fun modifyGuildRolePositions(
        @Path("guild-id") guildId: Long,
        @Body body: Array<ModifyGuildRolePositionBody>
    ): ArrayList<GuildRole>

    // MANAGE_ROLES
    @PATCH("guilds/{guild-id}/roles/{role-id}")
    suspend fun modifyGuildRole(
        @Path("guild-id") guildId: Long,
        @Path("role-id") roleId: Long,
        @Body body: CreateGuildRoleBody
    ): GuildRole

    // MANAGE_ROLES
    @DELETE("guilds/{guild-id}/roles/{role-id}")
    suspend fun deleteGuildRole(
        @Path("guild-id") guildId: Long,
        @Path("role-id") roleId: Long
    ): Response<Unit>

    // KICK_MEMBERS
    @GET("guilds/{guild-id}/prune")
    suspend fun getGuildPruneCount(
        @Path("guild-id") guildId: Long,
        @Query("days") days: Int? = null
    ): GuildPruneCount

    // KICK_MEMBERS
    @POST("guilds/{guild-id}/prune")
    suspend fun beginGuildPrune(
        @Path("guild-id") guildId: Long,
        @Query("days") days: Int? = null,
        @Query("compute_prune_count") computePruneCount: Boolean = true
    ): GuildPruneCount

    // @GET("guilds/{guild-id}/regions")
    // suspend fun getGuildVoiceRegions(
    //     @Path("guild-id") guildId: Long
    // ): ArrayList<JsonNode> // Voice Region

    // MANAGE_GUILD
    @GET("guilds/{guild-id}/invites")
    suspend fun getGuildInvites(
        @Path("guild-id") guildId: Long
    ): ArrayList<GuildInvite> // Invite

    // MANAGE_GUILD
    // @GET("guilds/{guild-id}/integrations")
    // suspend fun getGuildIntegrations(
    //     @Path("guild-id") guildId: Long
    // ): ArrayList<JsonNode> // Integration
    //
    // //MANAGE_GUILD
    // @POST("guilds/{guild-id}/integrations")
    // suspend fun createGuildIntegration(
    //     @Path("guild-id") guildId: Long
    // ): Response<Unit>
    //
    // //MANAGE_GUILD
    // @PATCH("guilds/{guild-id}/integrations/{integration-id}")
    // suspend fun modifyGuildIntegration(
    //     @Path("guild-id") guildId: Long,
    //     @Path("integration-id") integrationId: Long
    // ): Response<Unit>
    //
    // //MANAGE_GUILD
    // @DELETE("guilds/{guild-id}/integrations/{integration-id}")
    // suspend fun deleteGuildIntegration(
    //     @Path("guild-id") guildId: Long,
    //     @Path("integration-id") integrationId: Long
    // ): Response<Unit>

    // MANAGE_GUILD
    // @POST("guilds/{guild-id}/integrations/{integration-id}/sync")
    // suspend fun syncGuildIntegration(
    //     @Path("guild-id") guildId: Long,
    //     @Path("integration-id") integrationId: Long
    // ): Response<Unit>

    // MANAGE_GUILD
    @GET("guilds/{guild-id}/embed")
    suspend fun getGuildEmbed(
        @Path("guild-id") guildId: Long
    ): GuildEmbed // GuildEmbed

    // MANAGE_GUILD
    @PATCH("guilds/{guild-id}/embed")
    suspend fun modifyGuildEmbed(
        @Path("guild-id") guildId: Long,
        @Body body: GuildEmbed
    ): GuildEmbed // GuildEmbed

    // MANAGE_GUILD
    // REMARK: Code will be null if vanity url not set
    // @PATCH("guilds/{guild-id}/vanity-url")
    // suspend fun getGuildVanityUrl(
    //     @Path("guild-id") guildId: Long
    // ): JsonNode // Invite
    //
    // @GET("guilds/{guild-id}/widget.png")
    // suspend fun getGuildWidgetImage(
    //     @Path("guild-id") guildId: Long
    // ): JsonNode // PNG Image
}
