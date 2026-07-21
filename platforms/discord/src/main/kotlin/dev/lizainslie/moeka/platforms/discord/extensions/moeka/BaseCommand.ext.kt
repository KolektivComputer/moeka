package dev.lizainslie.moeka.platforms.discord.extensions.moeka

import dev.lizainslie.moeka.core.commands.BaseCommand
import dev.lizainslie.moeka.core.commands.CommandHandler
import dev.lizainslie.moeka.platforms.discord.commands.DiscordCommandContext

val BaseCommand.discordHandler: CommandHandler<DiscordCommandContext>?