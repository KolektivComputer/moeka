package dev.lizainslie.moeka.platforms.discord.entities

import dev.kord.common.entity.Snowflake
import dev.kord.core.entity.Message
import dev.lizainslie.moeka.core.platforms.PlatformId
import dev.lizainslie.moeka.core.platforms.entities.PlatformChannel
import dev.lizainslie.moeka.core.platforms.entities.PlatformMessage
import dev.lizainslie.moeka.core.platforms.entities.PlatformUser
import dev.lizainslie.moeka.platforms.discord.extensions.platform
import kotlinx.datetime.Instant

open class DiscordMessage(
    override val snowflake: Snowflake,
    override val content: String,
    override val authorId: PlatformId?,
    override val channelId: PlatformId,
    override val createdAt: Instant,
    override val id: PlatformId = snowflake.platform,
) : DiscordEntity, PlatformMessage {
    constructor(message: Message) : this(
        snowflake = message.id,
        content = message.content,
        authorId = message.author?.id?.platform,
        channelId = message.channelId.platform,
        createdAt = message.timestamp,
    )

    override val channel: PlatformChannel get() = TODO()
    override val author: DiscordUser? get() = TODO()
}
