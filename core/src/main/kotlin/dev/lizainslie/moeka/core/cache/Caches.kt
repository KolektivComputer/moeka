package dev.lizainslie.moeka.core.cache

import dev.lizainslie.moeka.core.cache.provider.CacheProvider
import dev.lizainslie.moeka.core.cache.accessor.PlatformEntityCacheAccessor
import dev.lizainslie.moeka.core.platforms.PlatformId
import dev.lizainslie.moeka.core.platforms.entities.PlatformChannel
import dev.lizainslie.moeka.core.platforms.entities.PlatformCommunity
import dev.lizainslie.moeka.core.platforms.entities.PlatformEntity
import dev.lizainslie.moeka.core.platforms.entities.PlatformMember
import dev.lizainslie.moeka.core.platforms.entities.PlatformMessage
import dev.lizainslie.moeka.core.platforms.entities.PlatformUser
import kotlin.reflect.KClass
import kotlin.time.Duration

class Caches(
    private val factory: CacheProvider,
) {
    private val caches = mutableMapOf<CacheNamespace, Cache<*, *>>()

    fun <K : Any, V : Any> createTyped(
        cacheNamespace: CacheNamespace,
        kClass: KClass<K>,
        vClass: KClass<V>,
        ttl: Duration? = null,
    ): Cache<K, V> = let {
        require(cacheNamespace !in caches) {
            "Cache namespace $cacheNamespace already exists"
        }

        factory.createTypedCache(cacheNamespace, kClass, vClass, ttl)
            .also { caches[cacheNamespace] = it }
    }

    fun <TEntity : PlatformEntity> createPlatformEntityCache(
        cacheNamespace: CacheNamespace,
        entityClass: KClass<TEntity>,
        ttl: Duration? = null,
    ) = createTyped(cacheNamespace, PlatformId::class, entityClass, ttl)

    fun <TEntity : PlatformEntity> createPlatformEntityCache(
        entityName: String,
        entityClass: KClass<TEntity>,
        ttl: Duration? = null,
    ) = createPlatformEntityCache(
        CacheNamespace("platform_entities:$entityName"),
        entityClass,
        ttl,
    )

    inline fun <reified TEntity : PlatformEntity> createPlatformEntityCache(
        cacheNamespace: CacheNamespace,
        ttl: Duration? = null,
    ) = createPlatformEntityCache(cacheNamespace, TEntity::class, ttl)

    inline fun <reified TEntity : PlatformEntity> createPlatformEntityCache(
        entityName: String,
        ttl: Duration? = null,
    ) = createPlatformEntityCache(entityName, TEntity::class, ttl)

    val users = PlatformEntityCacheAccessor(createPlatformEntityCache<PlatformUser>("users"))
    val members = PlatformEntityCacheAccessor(createPlatformEntityCache<PlatformMember>("members"))
    val roles = PlatformEntityCacheAccessor(createPlatformEntityCache<PlatformMember>("roles"))
    val communities = PlatformEntityCacheAccessor(createPlatformEntityCache<PlatformCommunity>("communities"))
    val channels = PlatformEntityCacheAccessor(createPlatformEntityCache<PlatformChannel>("channels"))
    val messages = PlatformEntityCacheAccessor(createPlatformEntityCache<PlatformMessage>("messages"))
}