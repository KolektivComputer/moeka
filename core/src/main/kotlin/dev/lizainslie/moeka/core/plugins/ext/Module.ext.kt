package dev.lizainslie.moeka.core.plugins.ext

import dev.lizainslie.moeka.core.plugins.PluginScopeArchetype
import dev.lizainslie.moeka.core.plugins.ext.koin.PluginScopeDsl
import dev.lizainslie.moeka.core.plugins.settings.PluginCommunitySettingsMap
import dev.lizainslie.moeka.core.plugins.settings.schema.SettingDefinition
import dev.lizainslie.moeka.core.plugins.types.AbstractPlugin
import dev.lizainslie.moeka.core.plugins.types.PluginManifest
import org.koin.core.annotation.KoinInternalApi
import org.koin.core.module.Module
import org.koin.core.parameter.parametersOf

@OptIn(KoinInternalApi::class)
inline fun <reified TPlugin : AbstractPlugin> Module.pluginScope(scopeSet: PluginScopeDsl<TPlugin>.() -> Unit) {
    PluginScopeDsl(PluginScopeArchetype, this, TPlugin::class).apply(scopeSet).apply {
        factory<PluginCommunitySettingsMap> { (definitions: List<SettingDefinition<*>>) ->
            PluginCommunitySettingsMap((sourceValue as AbstractPlugin).name, definitions)
        }

        scoped<PluginManifest> {
            get { parametersOf((sourceValue as AbstractPlugin).name) }
        }
    }
}