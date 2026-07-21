package dev.lizainslie.moeka.core.data.tables

import dev.lizainslie.moeka.core.modules.settings.schema.SETTING_KEY_MAX_LENGTH
import org.jetbrains.exposed.v1.core.dao.id.UuidTable

object PluginUserSettingsTable : UuidTable("plugin_user_settings") {
    val moduleName = varchar("plugin_name", 255).entityId()
    val platform = varchar("platform", 32).entityId()
    val userId = varchar("user_id", 255).entityId()

    val settingKey = varchar("setting_key", SETTING_KEY_MAX_LENGTH).entityId()

    val settingValue = text("setting_value").nullable().default(null)
}