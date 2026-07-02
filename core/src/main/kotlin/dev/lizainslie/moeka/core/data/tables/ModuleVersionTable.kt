package dev.lizainslie.moeka.core.data.tables

import org.jetbrains.exposed.v1.core.dao.id.IdTable

object ModuleVersionTable : IdTable<String>("module_versions") {
    override val id = varchar("module_name", 255).entityId().uniqueIndex()
    val version = varchar("version", 255)

    override val primaryKey = PrimaryKey(id)
}