package dev.lizainslie.moeka.core.platforms.entities

import dev.lizainslie.moeka.core.platforms.PlatformId

/**
 * A platform-agnostic representation of a channel
 */
interface PlatformChannel : PlatformEntity {
    /**
     * The name of this channel
     */
    val name: String

    /**
     * This channel's mention
     */
    val mention: PlatformMention

    /**
     * The ID of the [community] to which this channel belongs
     */
    val communityId: PlatformId?

    /**
     * The community to which this channel belongs
     *
     * Note: When overriding, use `get() = { ... }` syntax.
     */
    val community: PlatformCommunity?
}