package dev.lizainslie.moeka.core.commands

import dev.lizainslie.moeka.core.commands.dispatch.CommandDispatchService
import dev.lizainslie.moeka.core.commands.registration.CommandRegistrationService
import org.koin.dsl.module
import org.koin.plugin.module.dsl.single

val commandsModule = module {
    single<CommandRegistrationService>()
    single<CommandDispatchService>()
}