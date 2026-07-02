package dev.lizainslie.moeka.core.modules.settings.resolver

import dev.lizainslie.moeka.core.data.entities.ModuleCommunitySetting
import dev.lizainslie.moeka.core.modules.settings.schema.SettingDefinition
import dev.lizainslie.moeka.core.platforms.PlatformId
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

class ModuleCommunitySettingsHolder(
    val moduleName: String,
    definitions: List<SettingDefinition<*>>
) : SettingsHolder(definitions) {
    inline fun <reified TSettingValue : Any> getSetting(communityId: PlatformId, key: String): TSettingValue? {
        val definition = getDefinition<TSettingValue>(key)
        val setting = transaction { ModuleCommunitySetting.find(moduleName, communityId, key) }
        return setting?.getValue(definition) ?: definition.defaultValue
    }

    inline fun <reified TSettingValue : Any> getSettingRequired(communityId: PlatformId, key: String): TSettingValue {
        val definition = getDefinition<TSettingValue>(key)
        val setting = transaction { ModuleCommunitySetting.find(moduleName, communityId, key) }

        return setting?.getValue(definition)
            ?: definition.defaultValue
            ?: throw IllegalStateException("Required setting '$key' for module '$moduleName' in community '$communityId' is not set and has no default value.")
    }

    inline fun <reified TSettingValue: Any> setSetting(communityId: PlatformId, key: String, value: TSettingValue) {
        val definition = getDefinition<TSettingValue>(key)
        var setting = transaction { ModuleCommunitySetting.find(moduleName, communityId, key) }

        if (setting == null) {
            setting = transaction { ModuleCommunitySetting.new(moduleName, communityId, key) }
        }

        transaction { setting.setValue(definition, value) }
    }
}