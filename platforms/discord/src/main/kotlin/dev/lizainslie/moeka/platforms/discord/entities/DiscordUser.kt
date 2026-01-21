package dev.lizainslie.moeka.platforms.discord.entities

import dev.kord.core.entity.User
import dev.lizainslie.moeka.core.cache.accessor.PlatformEntityCacheAccessor
import dev.lizainslie.moeka.core.platforms.PlatformId
import dev.lizainslie.moeka.core.platforms.entities.PlatformCommunity
import dev.lizainslie.moeka.core.platforms.entities.PlatformMention
import dev.lizainslie.moeka.core.platforms.entities.PlatformUser
import dev.lizainslie.moeka.platforms.discord.Discord
import dev.lizainslie.moeka.platforms.discord.cache.DiscordEntityCacheWrapper
import dev.lizainslie.moeka.platforms.discord.cache.asDiscord
import dev.lizainslie.moeka.platforms.discord.extensions.platform

open class DiscordUser(
    val user: User,
) : DiscordEntity, PlatformUser {
    override val snowflake = user.id
    override val username = user.username
    override val displayName = user.globalName
    override val id: PlatformId = snowflake.platform
    override val mention: PlatformMention = DiscordMention.User(id)

    open val effectiveName: String get() = displayName ?: username

    override val mutualCommunities: PlatformEntityCacheAccessor<PlatformCommunity>
        get() = Discord.communities.asDiscord<DiscordCommunity>(
            fetch = { id ->
                Discord.getGuildById(id)?.let {
                    DiscordCommunity(
                        it
                    )
                }
            },
            predicate = { it.members.any { user -> user.id == id } }
        )
}