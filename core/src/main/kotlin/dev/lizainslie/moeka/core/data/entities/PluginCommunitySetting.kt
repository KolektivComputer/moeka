package dev.lizainslie.moeka.core.data.entities

import dev.lizainslie.moeka.core.data.tables.PluginCommunitySettingsTable
import dev.lizainslie.moeka.core.plugins.settings.schema.SettingDefinition
import dev.lizainslie.moeka.core.platforms.PlatformId
import org.jetbrains.exposed.v1.core.dao.id.CompositeID
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.CompositeEntity
import org.jetbrains.exposed.v1.dao.CompositeEntityClass

class PluginCommunitySetting(id: EntityID<CompositeID>) : CompositeEntity(id) {
    val moduleName by PluginCommunitySettingsTable.moduleName
    val platform by PluginCommunitySettingsTable.platform
    val communityId by PluginCommunitySettingsTable.communityId

    val settingKey by PluginCommunitySettingsTable.settingKey
    var settingValue by PluginCommunitySettingsTable.settingValue

    inline fun <reified TSettingValue : Any> getValue(def: SettingDefinition<TSettingValue>) =
        settingValue?.let { def.type.deserialize(it) } ?: def.defaultValue

    inline fun <reified TSettingValue : Any> setValue(def: SettingDefinition<TSettingValue>, value: TSettingValue?) {
        settingValue = def.type.serialize(value)
    }

    companion object : CompositeEntityClass<PluginCommunitySetting>(PluginCommunitySettingsTable) {
        fun find(moduleName: String, communityId: PlatformId, key: String) =
            findById(
                CompositeID { id ->
                    id[PluginCommunitySettingsTable.moduleName] = moduleName
                    id[PluginCommunitySettingsTable.platform] = communityId.platform.key
                    id[PluginCommunitySettingsTable.communityId] = communityId.id
                    id[PluginCommunitySettingsTable.settingKey] = key
                }
            )

        fun new(moduleName: String, communityId: PlatformId, key: String): PluginCommunitySetting =
            new(
                CompositeID { id ->
                    id[PluginCommunitySettingsTable.moduleName] = moduleName
                    id[PluginCommunitySettingsTable.platform] = communityId.platform.key
                    id[PluginCommunitySettingsTable.communityId] = communityId.id
                    id[PluginCommunitySettingsTable.settingKey] = key
                }
            ) {
                settingValue = null
            }
    }
}