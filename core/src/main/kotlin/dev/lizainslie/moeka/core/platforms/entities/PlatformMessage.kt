package dev.lizainslie.moeka.core.platforms.entities

import dev.lizainslie.moeka.core.platforms.PlatformId
import kotlinx.datetime.Instant

/**
 * A platform-agnostic representation of a message
 */
interface PlatformMessage : PlatformEntity {
    /**
     * The [content] of this message
     */
    val content: String

    /**
     * The timestamp at which this message was created
     */
    val createdAt: Instant

    /**
     * The ID of this message's [author]
     */
    val authorId: PlatformId?

    /**
     * This message's author.
     *
     * Note: When overriding, use `get() = { ... }` syntax.
     */
    val author: PlatformUser?

    /**
     * The ID of the [channel] in which this message was sent.
     */
    val channelId: PlatformId

    /**
     * The channel in which this message was sent.
     *
     * Note: When overriding, use `get() = { ... }` syntax.
     */
    val channel: PlatformChannel
}
