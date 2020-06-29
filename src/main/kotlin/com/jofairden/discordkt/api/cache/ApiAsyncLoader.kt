package com.jofairden.discordkt.api.cache

import com.github.benmanes.caffeine.cache.AsyncCacheLoader
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.future.asCompletableFuture

internal class ApiAsyncLoader<K, V>(
    private val block: suspend (key: K) -> V
) : AsyncCacheLoader<K, V> {
    override fun asyncLoad(key: K, executor: Executor): CompletableFuture<V> =
        /**
         *  The context used to call loadValue is composed of three things:
         *
         *  * IO Context
         *  * A new Job(), which overrides the job in the outer context. This is necessary to ensure that errors raised in loadValue don't propagate immediately but are instead captured in the CompletableFuture so that the cache can respond to them appropriately.
         *  * The executor, so that the configuration of the cache with regard to threads is still respected.
         *
         */
        CoroutineScope(Dispatchers.IO + Job() + executor.asCoroutineDispatcher()).async {
            block(key)
        }.asCompletableFuture()
}
