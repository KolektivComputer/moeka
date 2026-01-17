package dev.lizainslie.moeka.core.platforms.entities

/**
 * A platform-agnostic representation of a mention. Mentionables include:
 * - [Roles][PlatformRole]
 * - [Users][PlatformUser] / [Members][PlatformMember]
 * - [Channels][PlatformChannel]
 *
 * todo: maybe have implementors prefer a value class??
 */
interface PlatformMention {
    override fun toString(): String // must override.
}