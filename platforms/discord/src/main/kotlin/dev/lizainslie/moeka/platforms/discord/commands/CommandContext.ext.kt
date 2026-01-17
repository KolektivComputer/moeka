package dev.lizainslie.moeka.platforms.discord.commands

import dev.lizainslie.moeka.core.commands.CommandContext
import dev.lizainslie.moeka.core.platforms.UnsupportedPlatformException
import dev.lizainslie.moeka.platforms.discord.Discord

suspend inline fun <T : Any, reified TContext : DiscordCommandContext> CommandContext.evalDiscord(block: suspend TContext.() -> T): T? =
    if (this is TContext) {
        this.block()
    } else {
        null
    }

suspend inline fun <reified TContext : DiscordCommandContext> CommandContext.enforceDiscordType(block: suspend TContext.() -> Unit) {
    if (this is TContext) {
        this.block()
    } else {
        throw UnsupportedPlatformException(platform, Discord)
    }
}

suspend fun CommandContext.enforceDiscord(block: suspend DiscordCommandContext.() -> Unit) =
    enforceDiscordType<DiscordCommandContext>(block)

suspend fun CommandContext.enforceDiscordSlash(block: suspend DiscordSlashCommandContext.() -> Unit) =
    enforceDiscordType<DiscordSlashCommandContext>(block)
