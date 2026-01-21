package dev.lizainslie.moeka.core.cache.provider.memory

import dev.lizainslie.moeka.core.cache.Cache
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass
import kotlin.time.Duration

class InMemoryCache<K : Any, V : Any>(
    override val keyClass: KClass<K>,
    override val valueClass: KClass<V>,
    override val ttl: Duration? = null,
) : Cache<K, V> {
    private val innerCache = ConcurrentHashMap<K, V>()

    override val values: Collection<V> get() = innerCache.values
    override val entries get() = innerCache.entries
    override val size get() = innerCache.size

    override fun isEmpty() = innerCache.isEmpty()

    override fun put(key: K, value: V, ttl: Duration?) {
        innerCache[key] = value
    }

    override fun get(key: K) = innerCache[key]

    override fun containsKey(key: K) =
        innerCache.containsKey(key)

    override fun invalidate(key: K) {
        innerCache.remove(key)
    }

    override fun clear() {
        innerCache.clear()
    }
}