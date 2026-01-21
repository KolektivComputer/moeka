package dev.lizainslie.moeka.core.cache.provider

import dev.lizainslie.moeka.core.cache.Cache
import dev.lizainslie.moeka.core.cache.CacheNamespace
import kotlin.reflect.KClass
import kotlin.time.Duration

interface CacheProvider {
    fun <K : Any, V : Any> createTypedCache(
        namespace: CacheNamespace,
        kClass: KClass<K>,
        vClass: KClass<V>,
        ttl: Duration? = null
    ): Cache<K, V>
}