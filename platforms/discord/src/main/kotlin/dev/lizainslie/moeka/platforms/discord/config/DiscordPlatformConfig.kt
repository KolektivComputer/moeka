package dev.lizainslie.moeka.platforms.discord.config

import dev.lizainslie.moeka.core.config.ConfigBase
import dev.lizainslie.moeka.core.config.ConfigFile
import dev.lizainslie.moeka.core.config.ConfigType
import kotlinx.serialization.Serializable

@ConfigFile("discord.json", "platforms:discord", ConfigType.PLATFORM)
@Serializable
data class DiscordPlatformConfig(
    val token: String = "",
    val registerCommandsGlobally: Boolean = true,
    val guilds: List<DiscordGuildConfig> = emptyList(),
    val commandParsing: DiscordCommandParsingConfig = DiscordCommandParsingConfig(),
) : ConfigBase {
    override fun validate(): Boolean =
        (
            token.trim().isNotBlank() &&
                guilds.isNotEmpty() &&
                guilds.all {
                    it.validate()
                } &&
                commandParsing.validate()
        )
}
