package dev.lizainslie.moeka.core.cache

import kotlin.reflect.KClass
import kotlin.time.Duration

interface Cache<K : Any, V : Any> {
    val keyClass: KClass<K>
    val valueClass: KClass<V>

    val entries: Set<Map.Entry<K, V>>
    val values: Collection<V>

    val size: Int

    val ttl: Duration? get() = null

    fun isEmpty(): Boolean

    fun put(key: K, value: V, ttl: Duration? = this@Cache.ttl)
    operator fun get(key: K): V?

    fun containsKey(key: K): Boolean

    fun invalidate(key: K)
    fun clear()
}

inline fun <K : Any, V : Any> Cache<K, V>.getOrPut(
    key: K,
    loader: () -> V
): V {
    get(key)?.let { return it }

    val value = loader()
    put(key, value)
    return value
}
