package com.jofairden.kotlinkt.model.gateway

import com.jofairden.kotlinkt.model.gateway.OpAction.Receive
import com.jofairden.kotlinkt.model.gateway.OpAction.Send
import com.jofairden.kotlinkt.model.gateway.OpAction.SendAndReceive

sealed class OpCode(
    val code: Int,
    val action: OpAction
) {
    object Dispatch : OpCode(0, Receive)
    object Heartbeat : OpCode(1, SendAndReceive)
    object Identify : OpCode(2, Send)
    object StatusUpdate : OpCode(3, Send)
    object VoiceStateUpdate : OpCode(4, Send)
    object Resume : OpCode(6, Send)
    object Reconnect : OpCode(7, Receive)
    object RequestGuildMembers : OpCode(8, Send)
    object InvalidSession : OpCode(9, Receive)
    object Hello : OpCode(10, Receive)
    object HeartbeatACK : OpCode(11, Receive)

    companion object {
        fun find(op: Int) = OpCode::class.sealedSubclasses.first { it.objectInstance?.code == op }.objectInstance
    }
}
