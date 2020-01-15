package com.jofairden.discordkt.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

fun <T> lazyAsync(block: suspend CoroutineScope.() -> T): Lazy<Deferred<T>> {
    return lazy {
        GlobalScope.async(Dispatchers.Unconfined, start = CoroutineStart.LAZY) {
            block.invoke(this)
        }
    }
}
