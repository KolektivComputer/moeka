package dev.lizainslie.moeka.core.data.entities

import dev.lizainslie.moeka.core.data.tables.ModuleVersionTable
import io.github.z4kn4fein.semver.Version
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.Entity
import org.jetbrains.exposed.v1.dao.EntityClass

class ModuleVersion(id: EntityID<String>) : Entity<String>(id) {
    var moduleName by ModuleVersionTable.id
    private var _version by ModuleVersionTable.version

    var version: Version
        get() = Version.parse(_version)
        set(value) {
            _version = value.toString()
        }

    companion object : EntityClass<String, ModuleVersion>(ModuleVersionTable) {
        fun upsert(moduleName: String, version: Version): ModuleVersion {
            var moduleVersion = findById(moduleName)

            if (moduleVersion != null)
                moduleVersion.version = version

            else
                moduleVersion = new(moduleName) {
                    this.version = version
                }

            return moduleVersion
        }

        fun getVersion(moduleName: String) = findById(moduleName)?.version
    }
}