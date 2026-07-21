package dev.lizainslie.moeka.core.modules.settings

import dev.lizainslie.moeka.core.modules.settings.holder.PluginCommunitySettingsHolder
import dev.lizainslie.moeka.core.modules.settings.schema.SettingDefinition
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