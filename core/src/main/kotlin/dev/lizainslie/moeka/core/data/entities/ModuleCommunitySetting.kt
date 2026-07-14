package dev.lizainslie.moeka.core.data.entities

import dev.lizainslie.moeka.core.data.tables.ModuleCommunitySettingsTable
import dev.lizainslie.moeka.core.modules.settings.schema.SettingDefinition
import dev.lizainslie.moeka.core.platforms.PlatformId
import org.jetbrains.exposed.v1.core.dao.id.CompositeID
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.CompositeEntity
import org.jetbrains.exposed.v1.dao.CompositeEntityClass

class ModuleCommunitySetting(id: EntityID<CompositeID>) : CompositeEntity(id) {
    val moduleName by ModuleCommunitySettingsTable.moduleName
    val platform by ModuleCommunitySettingsTable.platform
    val communityId by ModuleCommunitySettingsTable.communityId

    val settingKey by ModuleCommunitySettingsTable.settingKey
    var settingValue by ModuleCommunitySettingsTable.settingValue

    inline fun <reified TSettingValue : Any> getValue(def: SettingDefinition<TSettingValue>) =
        settingValue?.let { def.type.deserialize(it) } ?: def.defaultValue

    inline fun <reified TSettingValue : Any> setValue(def: SettingDefinition<TSettingValue>, value: TSettingValue?) {
        settingValue = def.type.serialize(value)
    }

    companion object : CompositeEntityClass<ModuleCommunitySetting>(ModuleCommunitySettingsTable) {
        fun find(moduleName: String, communityId: PlatformId, key: String) =
            findById(
                CompositeID { id ->
                    id[ModuleCommunitySettingsTable.moduleName] = moduleName
                    id[ModuleCommunitySettingsTable.platform] = communityId.platform.key
                    id[ModuleCommunitySettingsTable.communityId] = communityId.id
                    id[ModuleCommunitySettingsTable.settingKey] = key
                }
            )

        fun new(moduleName: String, communityId: PlatformId, key: String): ModuleCommunitySetting =
            new(
                CompositeID { id ->
                    id[ModuleCommunitySettingsTable.moduleName] = moduleName
                    id[ModuleCommunitySettingsTable.platform] = communityId.platform.key
                    id[ModuleCommunitySettingsTable.communityId] = communityId.id
                    id[ModuleCommunitySettingsTable.settingKey] = key
                }
            ) {
                settingValue = null
            }
    }
}