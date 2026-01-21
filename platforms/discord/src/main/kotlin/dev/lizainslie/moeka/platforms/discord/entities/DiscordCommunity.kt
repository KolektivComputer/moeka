package dev.lizainslie.moeka.platforms.discord.entities

import dev.kord.core.entity.Guild
import dev.lizainslie.moeka.core.platforms.PlatformId
import dev.lizainslie.moeka.core.platforms.entities.PlatformCommunity
import dev.lizainslie.moeka.core.platforms.entities.PlatformMember
import dev.lizainslie.moeka.platforms.discord.Discord
import dev.lizainslie.moeka.platforms.discord.cache.DiscordEntityCacheWrapper
import dev.lizainslie.moeka.platforms.discord.cache.asDiscord
import dev.lizainslie.moeka.platforms.discord.extensions.platform
import kotlinx.coroutines.flow.firstOrNull

class DiscordCommunity(
    val guild: Guild,
) : DiscordEntity, PlatformCommunity {
    override val name = guild.name

    override val snowflake = guild.id
    override val id = snowflake.platform

    override val totalMembersCount: Int get() = TODO()
    override val userMembersCount: Int get() = TODO()
    override val botMembersCount: Int get() = TODO()

    override val ownerId = guild.ownerId.platform

    override val ownerUser get() = Discord.users[ownerId]
    override suspend fun getOwnerUser() = Discord.users.findByIdFetching(ownerId)
    override suspend fun getOwnerMember(platformId: PlatformId): PlatformMember? {
        TODO("Not yet implemented")
    }

    override val ownerMember = members[ownerId]

    override val members: DiscordEntityCacheWrapper<DiscordMember> get() =
        Discord.bot.caches.members.asDiscord<DiscordMember>(
            fetch = { id ->
                guild.members.firstOrNull { it.id.platform == id }?.let {
                    DiscordMember(
                        member = it,
                        community = this,
                        isOwner = it.isOwner(),
                        user = it.fetchUser()
                    )
                }
            },
            predicate = { it.communityId == id }
        )

}