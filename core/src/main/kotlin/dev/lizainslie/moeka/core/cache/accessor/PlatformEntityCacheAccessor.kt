package dev.lizainslie.moeka.core.cache.accessor

import dev.lizainslie.moeka.core.cache.Cache
import dev.lizainslie.moeka.core.platforms.AnyPlatformAdapter
import dev.lizainslie.moeka.core.platforms.PlatformId
import dev.lizainslie.moeka.core.platforms.entities.PlatformEntity
import dev.lizainslie.moeka.core.platforms.entities.PlatformUser
import javax.sql.rowset.Predicate

interface PlatformEntityCacheAccessor<TEntity : PlatformEntity> : CacheAccessor<PlatformId, TEntity> {
    override val inner: Cache<PlatformId, TEntity>
    override fun contains(element: TEntity) = inner[element.id] === element
    override fun containsAll(elements: Collection<TEntity>) = elements.all { contains(it) }

    fun filterByPlatform(platform: AnyPlatformAdapter) = filter {
        it.id.platform == platform.key
    }

    fun findById(id: PlatformId) = inner[id]

    open operator fun get(id: PlatformId): TEntity? = inner[id]
    fun filter(predicate: (PlatformEntity) -> Boolean): PlatformEntityCacheAccessor<TEntity> =
        FilterImpl(inner, predicate)


    private class Impl<TEntity : PlatformEntity>(
        override val inner: Cache<PlatformId, TEntity>,
    ) : PlatformEntityCacheAccessor<TEntity>

    private class FilterImpl<TEntity : PlatformEntity>(
        override val inner: Cache<PlatformId, TEntity>,
        private val predicate: (TEntity) -> Boolean,
    ) : PlatformEntityCacheAccessor<TEntity> {
        val filtered get() = inner.values.filter(predicate)

        override val size: Int
            get() = filtered.size
        override fun contains(element: TEntity) = filtered.contains(element)
        override fun iterator() = filtered.listIterator()
        override fun isEmpty() = filtered.isEmpty()

    }

    companion object {
        operator fun <TEntity : PlatformEntity> invoke(
            inner: Cache<PlatformId, TEntity>
        ): PlatformEntityCacheAccessor<TEntity> = Impl(inner)
    }
}