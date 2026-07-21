package dev.lizainslie.moeka.core.commands

import dev.lizainslie.moeka.core.plugins.AbstractPlugin

data class CommandRegistration(
    val command: RootCommand,
    val plugin: AbstractPlugin,
)
