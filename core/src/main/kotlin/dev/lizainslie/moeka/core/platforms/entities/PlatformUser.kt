package dev.lizainslie.moeka.core.platforms.entities

import dev.lizainslie.moeka.core.platforms.PlatformId

/**
 * A platform-agnostic representation of a user
 */
interface PlatformUser : PlatformEntity {
    /**
     * The user's username.
     */
    val username: String

    /**
     * The user's display name
     */
    val displayName: String

    /**
     * The user's mention.
     */
    val mention: PlatformMention

    /**
     * IDs of the communities we share with the user
     */
    val mutualCommunityIds: List<PlatformId>

    // todo: mutualCommunities accessor. maybe use a flow?
}