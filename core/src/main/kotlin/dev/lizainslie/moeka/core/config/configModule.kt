package dev.lizainslie.moeka.core.config

import org.koin.dsl.module
import org.koin.plugin.module.dsl.single

val configModule = module {
    single<ConfigService>()
}