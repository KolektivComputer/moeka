package dev.lizainslie.moeka.core.data.entities

import dev.lizainslie.moeka.core.data.tables.CustomCommunityPrefixTable
import dev.lizainslie.moeka.core.platforms.PlatformId
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.dao.id.CompositeID
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.dao.CompositeEntity
import org.jetbrains.exposed.v1.dao.CompositeEntityClass


class CustomCommunityPrefix(
    id: EntityID<CompositeID>,
) : CompositeEntity(id) {
    companion object : CompositeEntityClass<CustomCommunityPrefix>(CustomCommunityPrefixTable) {
        fun getPrefix(communityId: PlatformId): CustomCommunityPrefix? =
            find {
                (CustomCommunityPrefixTable.platform eq communityId.platform.key) and
                    (CustomCommunityPrefixTable.communityId eq communityId.id)
            }.firstOrNull()
    }

    var platform by CustomCommunityPrefixTable.platform
    var communityId by CustomCommunityPrefixTable.communityId
    var prefix by CustomCommunityPrefixTable.prefix
}
