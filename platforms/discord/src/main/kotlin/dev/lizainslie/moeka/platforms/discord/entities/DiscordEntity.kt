package dev.lizainslie.moeka.platforms.discord.entities

import dev.kord.common.entity.Snowflake
import dev.lizainslie.moeka.core.platforms.entities.PlatformEntity

interface DiscordEntity : PlatformEntity {
    val snowflake: Snowflake
}