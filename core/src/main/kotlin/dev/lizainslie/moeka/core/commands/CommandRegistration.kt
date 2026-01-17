package dev.lizainslie.moeka.core.commands

import dev.lizainslie.moeka.core.modules.AbstractModule

data class CommandRegistration(
    val command: RootCommand,
    val module: AbstractModule,
)
