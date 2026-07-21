package dev.lizainslie.moeka.platforms.discord.entities

import dev.kord.core.entity.Message
import dev.lizainslie.moeka.core.platforms.PlatformId
import dev.lizainslie.moeka.core.platforms.entities.PlatformMessage
import dev.lizainslie.moeka.platforms.discord.extensions.kord.platform

open class DiscordMessage(
    override val content: String,
    override val authorId: PlatformId,
    val channelId: PlatformId,
) : PlatformMessage {
    constructor(message: Message) : this(
        content = message.content,
        authorId = message.author!!.id.platform,
        channelId = message.channelId.platform,
    )
}
