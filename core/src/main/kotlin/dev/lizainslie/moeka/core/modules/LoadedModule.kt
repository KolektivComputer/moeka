package dev.lizainslie.moeka.core.modules

data class LoadedModule(
    val name: String,
    val instance: AbstractModule,
    val classLoader: ModuleClassLoader?,
    val source: ModuleSource,
)
