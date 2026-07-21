package dev.lizainslie.moeka.core.plugins.registry

import dev.lizainslie.moeka.core.plugins.types.AbstractPlugin
import dev.lizainslie.moeka.core.plugins.types.PluginSource
import org.koin.core.module.Module
import org.koin.core.scope.Scope

data class LoadedPlugin(
    val name: String,
    val instance: AbstractPlugin,
    val module: Module,
    val classLoader: PluginClassLoader?,
    val source: PluginSource,
    val scope: Scope,
)