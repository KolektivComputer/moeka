package dev.lizainslie.moeka.core.platforms.entities

import dev.lizainslie.moeka.core.platforms.PlatformId

interface PlatformMessage {
    val content: String
    val authorId: PlatformId
}
