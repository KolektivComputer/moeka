package dev.lizainslie.moeka.core.commands

typealias CommandHandler<TContext> = suspend TContext.() -> Unit