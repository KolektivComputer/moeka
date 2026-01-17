package dev.lizainslie.moeka.core.platforms.entities

import dev.lizainslie.moeka.core.commands.CommandContext

interface PlatformResponse {
    val context: CommandContext

    suspend fun edit(newContent: String)

    suspend fun createFollowup(content: String): PlatformResponse

    suspend fun createPrivateFollowup(content: String): PlatformResponse

    suspend fun createStealthFollowup(content: String): PlatformResponse
}
