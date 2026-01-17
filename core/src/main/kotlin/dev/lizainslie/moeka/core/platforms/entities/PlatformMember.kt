package dev.lizainslie.moeka.core.platforms.entities

import dev.lizainslie.moeka.core.platforms.PlatformId

/**
 * A platform-agnostic representation of a member of a [community][PlatformCommunity]
 */
interface PlatformMember : PlatformUser {
    /**
     * The ID of the [community] to which this member belongs
     */
    val communityId: PlatformId

    /**
     * The community to which this member belongs.
     *
     * Note: When overriding, use `get() = { ... }` syntax.
     */
    val community: PlatformCommunity
}