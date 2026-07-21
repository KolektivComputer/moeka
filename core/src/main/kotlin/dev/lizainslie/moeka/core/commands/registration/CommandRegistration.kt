package dev.lizainslie.moeka.core.commands.registration

import dev.lizainslie.moeka.core.commands.RootCommand
import dev.lizainslie.moeka.core.plugins.types.AbstractPlugin

data class CommandRegistration(
    val command: RootCommand,
    val plugin: AbstractPlugin,
)
