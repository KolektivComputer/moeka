package dev.lizainslie.moeka.platforms.discord.cache

import dev.lizainslie.moeka.core.cache.Cache
import dev.lizainslie.moeka.core.cache.accessor.PlatformEntityCacheAccessor
import dev.lizainslie.moeka.core.platforms.PlatformId
import dev.lizainslie.moeka.core.platforms.entities.PlatformEntity
import dev.lizainslie.moeka.platforms.discord.Discord
import dev.lizainslie.moeka.platforms.discord.entities.DiscordEntity
import kotlin.time.Duration

class DiscordEntityCacheWrapper<TEntity : DiscordEntity>(
    override val inner: Cache<PlatformId, TEntity>,
    private val predicate: (TEntity) -> Boolean = { true },
    private val fetch: suspend (id: PlatformId) -> TEntity?,
) : PlatformEntityCacheAccessor<TEntity> {

    override fun iterator(): Iterator<TEntity> =
        inner.entries
            .asSequence()
            .filter { it.key.platform == Discord.key && predicate(it.value) }
            .map { it.value }
            .iterator()

    override val size: Int
        get() = inner.entries.count { it.key.platform == Discord.key && predicate(it.value) }

    suspend fun findByIdFetching(id: PlatformId, ttl: Duration? = null): TEntity? {
        if (inner[id] != null) return inner[id]

        // inner doesn't have it, so we try and fetch, todo: implement cache miss event
        val fetchResult = fetch(id)
        if (fetchResult != null) {
            inner.put(id, fetchResult, ttl)
            return fetchResult
        }

        // todo: implement fetch fail event

        return null
    }
}

inline fun <reified TEntity : DiscordEntity> PlatformEntityCacheAccessor<out PlatformEntity>.asDiscord(
    noinline fetch: suspend (id: PlatformId) -> TEntity?,
    noinline predicate: (TEntity) -> Boolean = { true },
): DiscordEntityCacheWrapper<TEntity> {
    @Suppress("UNCHECKED_CAST")
    val typedInner = inner as Cache<PlatformId, TEntity>

    return DiscordEntityCacheWrapper(
        inner = typedInner,
        predicate = predicate,
        fetch = fetch,
    )
}


inline fun <reified TEntity : DiscordEntity> PlatformEntityCacheAccessor<out PlatformEntity>.asDiscord(
    noinline fetch: suspend (id: PlatformId) -> TEntity?,
): DiscordEntityCacheWrapper<TEntity> {
    @Suppress("UNCHECKED_CAST")
    val typedInner = inner as Cache<PlatformId, TEntity>

    return DiscordEntityCacheWrapper(
        inner = typedInner,
        fetch = fetch,
    )
}
