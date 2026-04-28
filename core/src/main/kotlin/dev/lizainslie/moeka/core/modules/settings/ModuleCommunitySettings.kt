package dev.lizainslie.moeka.core.modules.settings

import dev.lizainslie.moeka.core.platforms.PlatformId

interface ModuleCommunitySettings : ModuleSettings {
    val communityId: PlatformId
}