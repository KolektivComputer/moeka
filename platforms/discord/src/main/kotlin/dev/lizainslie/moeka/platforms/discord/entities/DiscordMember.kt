package dev.lizainslie.moeka.platforms.discord.entities

import dev.kord.common.annotation.KordPreview
import dev.kord.common.entity.Snowflake
import dev.kord.core.behavior.edit
import dev.kord.core.entity.Member
import dev.kord.core.entity.User
import dev.lizainslie.moeka.core.platforms.PlatformId
import dev.lizainslie.moeka.core.platforms.entities.PlatformMember
import dev.lizainslie.moeka.core.platforms.entities.PlatformMention
import dev.lizainslie.moeka.platforms.discord.extensions.platform
import kotlinx.coroutines.coroutineScope

class DiscordMember(
    val member: Member,
    override val community: DiscordCommunity,
    override val isOwner: Boolean,
    user: User,
) : DiscordUser(user), PlatformMember {
    override val snowflake: Snowflake = member.id
    override val id: PlatformId = snowflake.platform

    override val mention: PlatformMention = DiscordMention.Member(id)

    override val communityId: PlatformId = member.guild.id.platform

    override val username = user.username
    override val displayName: String? = user.globalName
    override val effectiveName = nickname ?: super.effectiveName

    private var _nickname: String? = member.nickname
    override val nickname get() = _nickname

    override suspend fun setNickname(nickname: String?) {
        _nickname = nickname
        member.edit {
            this.nickname = _nickname
        }
    }
}