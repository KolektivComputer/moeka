package dev.lizainslie.moeka.core.data.tables

import org.jetbrains.exposed.v1.core.dao.id.CompositeIdTable

object DeveloperOptionsTable : CompositeIdTable("developer_options") {
    val platform = varchar("platform", 32).entityId()
    val userId = varchar("user_id", 255).uniqueIndex().entityId()
    val stealth = bool("stealth").default(false)
    val contextDebug = bool("context_debug").default(false)

    override val primaryKey = PrimaryKey(platform, userId)
}
