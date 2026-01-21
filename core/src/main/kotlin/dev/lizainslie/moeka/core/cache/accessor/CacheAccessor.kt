package dev.lizainslie.moeka.core.cache.accessor

import dev.lizainslie.moeka.core.cache.Cache
interface CacheAccessor<K : Any, V : Any> : Collection<V> {
    val inner: Cache<K, V>

    override val size get() = inner.size
    override fun isEmpty() = inner.isEmpty()
    override fun iterator() = inner.values.iterator()
}