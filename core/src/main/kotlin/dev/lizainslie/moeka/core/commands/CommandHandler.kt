package dev.lizainslie.moeka.core.commands

typealias CommandHandler = suspend CommandContext.() -> Unit
