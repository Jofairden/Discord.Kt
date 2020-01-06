package com.jofairden.kotlinkt.model

sealed class GatewayCloseEventCode(
    val code: Int
) {
    object UnknownError : GatewayCloseEventCode(4000)
    object UnknownOpCode : GatewayCloseEventCode(4001)
    object DecodeError : GatewayCloseEventCode(4002)
    object NotAuthenticated : GatewayCloseEventCode(4003)
    object AuthenticationFailed : GatewayCloseEventCode(4004)
    object AlreadyAuthenticated : GatewayCloseEventCode(4005)
    object InvalidSeq : GatewayCloseEventCode(4007)
    object RateLimited : GatewayCloseEventCode(4008)
    object SessionTimeout : GatewayCloseEventCode(4009)
    object InvalidShard : GatewayCloseEventCode(4010)
    object ShardingRequired : GatewayCloseEventCode(4011)
}
