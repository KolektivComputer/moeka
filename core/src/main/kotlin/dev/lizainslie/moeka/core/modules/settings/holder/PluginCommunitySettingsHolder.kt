package dev.lizainslie.moeka.core.modules.settings.holder

import dev.lizainslie.moeka.core.data.entities.ModuleCommunitySetting
import dev.lizainslie.moeka.core.modules.settings.schema.SettingDefinition
import dev.lizainslie.moeka.core.platforms.PlatformId
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

class PluginCommunitySettingsHolder(
    val moduleName: String,
    val communityId: PlatformId,
    definitions: List<SettingDefinition<*>>
) : AbstractSettingsHolder(definitions) {
    inline fun <reified TSettingValue : Any> getSetting(key: String): TSettingValue? {
        val definition = getDefinition<TSettingValue>(key)
        val setting = transaction { ModuleCommunitySetting.find(moduleName, communityId, key) }
        return setting?.getValue(definition) ?: definition.defaultValue
    }

    inline operator fun <reified TSettingValue : Any> get(key: String) = getSetting<TSettingValue>(key)

    inline fun <reified TSettingValue : Any> getSettingRequired(key: String): TSettingValue {
        val definition = getDefinition<TSettingValue>(key)
        val setting = transaction { ModuleCommunitySetting.find(moduleName, communityId, key) }

        return setting?.getValue(definition)
            ?: definition.defaultValue
            ?: throw IllegalStateException("Required setting '$key' for module '$moduleName' in community '$communityId' is not set and has no default value.")
    }

    inline fun <reified TSettingValue: Any> setSetting(key: String, value: TSettingValue?) {
        val definition = getDefinition<TSettingValue>(key)
        var setting = transaction { ModuleCommunitySetting.find(moduleName, communityId, key) }

        if (setting == null)
            setting = transaction { ModuleCommunitySetting.new(moduleName, communityId, key) }

        transaction { setting.setValue(definition, value) }
    }

    inline operator fun <reified TSettingValue : Any> set(key: String, value: TSettingValue?) {
        setSetting(key, value)
    }
}