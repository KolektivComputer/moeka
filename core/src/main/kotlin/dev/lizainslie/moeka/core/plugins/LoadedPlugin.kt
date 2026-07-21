package dev.lizainslie.moeka.core.plugins

data class LoadedPlugin(
    val name: String,
    val instance: AbstractPlugin,
    val classLoader: PluginClassLoader?,
    val source: PluginSource,
)
