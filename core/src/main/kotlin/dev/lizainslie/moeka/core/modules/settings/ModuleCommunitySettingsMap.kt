package dev.lizainslie.moeka.core.modules.settings

import dev.lizainslie.moeka.core.modules.settings.holder.ModuleCommunitySettingsHolder
import dev.lizainslie.moeka.core.modules.settings.schema.SettingDefinition
import dev.lizainslie.moeka.core.platforms.PlatformId

class ModuleCommunitySettingsMap(
    private val moduleName: String,
    private val definitions: List<SettingDefinition<*>>
) {
    private val internalMap = mutableMapOf<PlatformId, ModuleCommunitySettingsHolder>()

    operator fun get(communityId: PlatformId) = internalMap.getOrPut(communityId) {
        ModuleCommunitySettingsHolder(moduleName, communityId, definitions)
    }
}