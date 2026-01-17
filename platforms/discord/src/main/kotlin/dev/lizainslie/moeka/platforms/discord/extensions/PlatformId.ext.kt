package dev.lizainslie.moeka.platforms.discord.extensions

import dev.kord.common.entity.Snowflake
import dev.lizainslie.moeka.core.platforms.PlatformId

val PlatformId.snowflake get() = Snowflake(this.id)
