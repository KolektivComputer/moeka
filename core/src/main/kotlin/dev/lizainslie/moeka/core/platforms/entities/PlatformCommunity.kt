package dev.lizainslie.moeka.core.platforms.entities

import dev.lizainslie.moeka.core.cache.accessor.PlatformEntityCacheAccessor
import dev.lizainslie.moeka.core.platforms.PlatformId

/**
 * A platform-agnostic representation of a community.
 */
interface PlatformCommunity : PlatformEntity {
    /**
     * The name of this community
     */
    val name: String

    /**
     * Total count of all members in this community
     */
    val totalMembersCount: Int

    /**
     * Total count of all members who are users
     */
    val userMembersCount: Int

    /**
     * Total count of all members who are bots
     */
    val botMembersCount: Int

    /**
     * The ID of the owner of this community
     */
    val ownerId: PlatformId

    /**
     * The owner of this community as a user
     *
     * Note: When overriding, use `get() = { ... }` syntax.
     */
    val ownerUser: PlatformUser?

    /**
     * The owner of this community as a member
     *
     * Note: When overriding, use `get() = { ... }` syntax.
     */
    val ownerMember: PlatformMember?

    suspend fun getOwnerUser(): PlatformUser?
    suspend fun getOwnerMember(platformId: PlatformId): PlatformMember?

    // todo: getters/flows/other access for channels, members.
    val members: PlatformEntityCacheAccessor<PlatformMember>
}