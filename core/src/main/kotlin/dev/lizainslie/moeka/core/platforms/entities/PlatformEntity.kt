package dev.lizainslie.moeka.core.platforms.entities

import dev.lizainslie.moeka.core.platforms.PlatformId

/**
 * Base interface for a platform-agnostic entity representation.
 */
interface PlatformEntity {
    /**
     * The ID of this entity
     */
    val id: PlatformId
}