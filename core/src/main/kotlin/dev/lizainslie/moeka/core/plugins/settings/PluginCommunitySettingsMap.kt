package dev.lizainslie.moeka.core.plugins.settings

import dev.lizainslie.moeka.core.plugins.settings.holder.PluginCommunitySettingsHolder
import dev.lizainslie.moeka.core.plugins.settings.schema.SettingDefinition
import dev.lizainslie.moeka.core.platforms.PlatformId

class PluginCommunitySettingsMap(
    private val moduleName: String,
    private val definitions: List<SettingDefinition<*>>
) {
    private val internalMap = mutableMapOf<PlatformId, PluginCommunitySettingsHolder>()

    operator fun get(communityId: PlatformId) = internalMap.getOrPut(communityId) {
        PluginCommunitySettingsHolder(moduleName, communityId, definitions)
    }
}