package dev.lizainslie.moeka.platforms.discord.entities

import dev.kord.common.entity.Snowflake
import dev.lizainslie.moeka.core.platforms.PlatformId
import dev.lizainslie.moeka.core.platforms.entities.PlatformMention

sealed interface DiscordMention : PlatformMention {
    class User(override val id: PlatformId) : DiscordMention {
        override fun toString() = "<@${id.id}>"
    }

    class Member(override val id: PlatformId) : DiscordMention {
        override fun toString() = "<@${id.id}>" // todo: research other formats
    }
}