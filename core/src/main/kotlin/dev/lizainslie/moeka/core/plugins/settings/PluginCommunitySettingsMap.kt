package dev.lizainslie.moeka.core.plugins.settings

import dev.lizainslie.moeka.core.plugins.settings.holder.PluginCommunitySettingsHolder
import dev.lizainslie.moeka.core.plugins.settings.schema.SettingDefinition
import dev.lizainslie.moeka.core.platforms.PlatformId
import org.koin.core.component.KoinComponent

class PluginCommunitySettingsMap(
    private val pluginName: String,
    private val definitions: List<SettingDefinition<*>>
) : KoinComponent {
    private val internalMap = mutableMapOf<PlatformId, PluginCommunitySettingsHolder>()

    operator fun get(communityId: PlatformId) = internalMap.getOrPut(communityId) {
        PluginCommunitySettingsHolder(pluginName, communityId, definitions)
    }
}