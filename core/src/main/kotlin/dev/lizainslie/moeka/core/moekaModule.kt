package dev.lizainslie.moeka.core

import dev.lizainslie.moeka.core.commands.commandsModule
import dev.lizainslie.moeka.core.config.configModule
import dev.lizainslie.moeka.core.fs.fsModule
import org.koin.dsl.module
import org.koin.plugin.module.dsl.single

val moekaModule = module {
    includes(commandsModule, fsModule, configModule)

    single<Bot>()
}