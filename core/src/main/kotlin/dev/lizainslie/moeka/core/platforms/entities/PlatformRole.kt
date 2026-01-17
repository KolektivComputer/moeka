package dev.lizainslie.moeka.core.platforms.entities

import dev.lizainslie.moeka.core.platforms.PlatformId

/**
 * A platform-agnostic representation of a role
 */
interface PlatformRole : PlatformEntity {
    /**
     * The name of this role
     */
    val name: String

    /**
     * This role's mention
     */
    val mention: PlatformMention

    /**
     * The ID of the [community] to which this role belongs
     */
    val communityId: PlatformId

    /**
     * The community to which this role belongs
     *
     * Note: When overriding, use `get() = { ... }` syntax.
     */
    val community: PlatformCommunity
}