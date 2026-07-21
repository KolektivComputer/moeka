package dev.lizainslie.moeka.core.plugins

import dev.lizainslie.moeka.core.platforms.PlatformId
import dev.lizainslie.moeka.core.plugins.registry.PluginRegistryService
import dev.lizainslie.moeka.core.plugins.settings.PluginCommunitySettingsMap
import dev.lizainslie.moeka.core.plugins.settings.schema.SettingDefinition
import dev.lizainslie.moeka.core.plugins.types.AbstractPlugin
import dev.lizainslie.moeka.core.plugins.types.PluginManifest
import org.koin.core.annotation.KoinInternalApi
import org.koin.dsl.module
import org.koin.plugin.module.dsl.single

@OptIn(KoinInternalApi::class)
val pluginsModule = module {
    single<PluginRegistryService>()
    single<PluginManifestService>()

    factory<PluginManifest> { (pluginName: String) ->
        get<PluginManifestService>()[pluginName]
            ?: error("Could not find manifest for plugin: '$pluginName'.")
    }
}