package dev.lizainslie.moeka.core.fs

import org.koin.dsl.module
import org.koin.plugin.module.dsl.single

val fsModule = module {
    single<BotFs>()
    single<TempFs>()
}