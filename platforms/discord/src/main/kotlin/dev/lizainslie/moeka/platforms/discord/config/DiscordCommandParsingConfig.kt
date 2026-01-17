package dev.lizainslie.moeka.platforms.discord.config

import dev.lizainslie.moeka.core.config.ConfigBase
import kotlinx.serialization.Serializable

@Serializable
data class DiscordCommandParsingConfig(
    val allowMentionPrefix: Boolean = true,
    val allowCustomGuildPrefixes: Boolean = true,
    val allowCustomUserPrefixes: Boolean = true,
) : ConfigBase {
    override fun validate() = true
}
