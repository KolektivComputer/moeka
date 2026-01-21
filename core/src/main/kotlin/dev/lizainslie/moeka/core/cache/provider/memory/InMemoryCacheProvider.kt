package dev.lizainslie.moeka.core.cache.provider.memory

import dev.lizainslie.moeka.core.cache.CacheNamespace
import dev.lizainslie.moeka.core.cache.provider.CacheProvider
import kotlin.reflect.KClass
import kotlin.time.Duration

class InMemoryCacheProvider : CacheProvider {
    override fun <K : Any, V : Any> createTypedCache(
        namespace: CacheNamespace,
        kClass: KClass<K>,
        vClass: KClass<V>,
        ttl: Duration?
    ) = InMemoryCache(kClass, vClass, ttl)
}