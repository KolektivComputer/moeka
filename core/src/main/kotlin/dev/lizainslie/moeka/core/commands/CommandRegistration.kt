package dev.lizainslie.moeka.core.commands

import dev.lizainslie.moeka.core.modules.AbstractPlugin

data class CommandRegistration(
    val command: RootCommand,
    val module: AbstractPlugin,
)
