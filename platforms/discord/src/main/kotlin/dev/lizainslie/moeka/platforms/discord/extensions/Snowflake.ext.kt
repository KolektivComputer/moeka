package dev.lizainslie.moeka.platforms.discord.extensions

import dev.kord.common.entity.Snowflake
import dev.lizainslie.moeka.core.platforms.PlatformId
import dev.lizainslie.moeka.platforms.discord.Discord

val Snowflake.platform get() =
    PlatformId(
        platform = Discord.key,
        id = value.toString(),
    )

infix fun Snowflake.eq(other: PlatformId) = this.value == other.id.toULong()
